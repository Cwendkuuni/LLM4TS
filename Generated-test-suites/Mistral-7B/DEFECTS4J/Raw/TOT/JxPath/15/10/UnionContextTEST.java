import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext unionContext;
 private Document document;

 @Before
 public void setUp() {
 contextA = new EvalContext(null, null);
 contextB = new EvalContext(null, null);
 document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

 contextA.setDocument(document);
 contextB.setDocument(document);

 // Setup contexts with some nodes
 setupContextA();
 setupContextB();
 }

 private void setupContextA() {
 Element rootA = document.createElement("rootA");
 document.appendChild(rootA);

 Element a1 = document.createElement("a1");
 a1.setTextContent("A1");
 rootA.appendChild(a1);

 Element a2 = document.createElement("a2");
 a2.setTextContent("A2");
 rootA.appendChild(a2);

 contextA.setCurrentNodePointer(new NodePointer(a1));
 contextA.nextNode(); // move to a2
 }

 private void setupContextB() {
 Element rootB = document.createElement("rootB");
 document.appendChild(rootB);

 Element b1 = document.createElement("b1");
 b1.setTextContent("B1");
 rootB.appendChild(b1);

 contextB.setCurrentNodePointer(new NodePointer(b1));
 }

 @Test
 public void testUnionContext_PositiveCase() {
 unionContext = new UnionContext(contextA, contextB);
 unionContext.nextSet(); // move to first set
 unionContext.nextNode(); // move to first node in the first set

 assertEquals("A1", unionContext.getValue(null).getNodeValue());

 unionContext.nextSet(); // move to the second set
 unionContext.nextNode(); // move to the first node in the second set

 assertEquals("B1", unionContext.getValue(null).getNodeValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnionContext_ErrorCase_NoCommonNodes() {
 unionContext = new UnionContext(contextA, contextB);
 unionContext.nextSet(); // move to the first set
 unionContext.nextNode(); // move to the end of the first set
 unionContext.nextSet(); // move to the second set
 unionContext.nextNode(); // move to the end of the second set

 assertFalse(unionContext.hasNext());
 }

 @Test
 public void testUnionContext_EdgeCase_FirstContextEmpty() {
 contextB.clear(); // make contextB empty
 unionContext = new UnionContext(contextA, contextB);
 unionContext.nextSet(); // move to the first set
 unionContext.nextNode(); // move to the first node in the first set

 assertEquals("A1", unionContext.getValue(null).getNodeValue());

 contextB.setCurrentNodePointer(new NodePointer(document.createTextNode("B2")));
 contextB.nextNode();

 unionContext.nextSet(); // move to the second set (now it's not empty)
 unionContext.nextNode(); // move to the first node in the second set

 assertEquals("B2", unionContext.getValue(null).getNodeValue());
 }

 @Test
 public void testUnionContext_EdgeCase_SecondContextEmpty() {
 contextA.clear(); // make contextA empty
 unionContext = new UnionContext(contextA, contextB);
 unionContext.nextSet(); // move to the first set (now it's empty)
 unionContext.nextNode(); // move to the end of the empty set

 assertFalse(unionContext.hasNext());

 contextA.setCurrentNodePointer(new NodePointer(document.createTextNode("A2")));
 contextA.nextNode();

 unionContext.nextSet(); // move to the second set
 unionContext.nextNode(); // move to the first node in the second set

 assertEquals("B1", unionContext.getValue(null).getNodeValue());
 }
}