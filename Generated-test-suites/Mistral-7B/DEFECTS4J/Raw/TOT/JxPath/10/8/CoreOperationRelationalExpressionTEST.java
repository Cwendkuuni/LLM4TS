import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.axes.SelfAxis;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.ITestable;
import org.apache.commons.jxpath.ri.NameContext;
import org.apache.commons.jxpath.ri.nodes.NodeInfo;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest implements ITestable {

 private CoreOperationRelationalExpression expression;
 private JXPathContext context;
 private NameContext nameContext;
 private Element root;

 @Before
 public void setUp() {
 root = new Document().createElement("root");
 nameContext = new NameContext(Axis.SELF, new SelfAxis(), root);
 context = new JXPathContext(root, nameContext);
 expression = new CoreOperationRelationalExpression(new CoreOperationIdentifier("a"), new CoreOperationIdentifier("b")) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 }

 @Test
 public void testGreaterThan() {
 NodeInfo a = context.setContextItems(NodeInfo.createNodeInfo(new Integer(3))).jxPath("$[1]", new CoreExpressionIdentifier("a")).getValue();
 NodeInfo b = context.jxPath("$[1]", new CoreExpressionIdentifier("b")).getValue();
 assertTrue(expression.computeValue(context));
 context.clearVariables();
 assertFalse(expression.computeValue(context));
 context.setContextItem("a", a);
 context.setContextItem("b", b);
 assertTrue(expression.computeValue(context));
 context.clearVariables();
 }

 @Test
 public void testLessThan() {
 NodeInfo a = context.setContextItems(NodeInfo.createNodeInfo(new Integer(1))).jxPath("$[1]", new CoreExpressionIdentifier("a")).getValue();
 NodeInfo b = context.jxPath("$[1]", new CoreExpressionIdentifier("b")).getValue();
 assertFalse(expression.computeValue(context));
 context.clearVariables();
 assertFalse(expression.computeValue(context));
 context.setContextItem("a", a);
 context.setContextItem("b", b);
 assertFalse(expression.computeValue(context));
 context.clearVariables();
 }

 @Test
 public void testGreaterThanOrEqual() {
 NodeInfo a = context.setContextItems(NodeInfo.createNodeInfo(new Integer(3))).jxPath("$[1]", new CoreExpressionIdentifier("a")).getValue();
 NodeInfo b = context.jxPath("$[1]", new CoreExpressionIdentifier("b")).getValue();
 NodeInfo c = context.setContextItems(NodeInfo.createNodeInfo(new Integer(3))).jxPath("$[1]", new CoreExpressionIdentifier("c")).getValue();
 assertTrue(expression.computeValue(context, new Object[]{a, b}));
 assertFalse(expression.computeValue(context, new Object[]{a, c}));
 context.clearVariables();
 }

 @Test
 public void testLessThanOrEqual() {
 NodeInfo a = context.setContextItems(NodeInfo.createNodeInfo(new Integer(1))).jxPath("$[1]", new CoreExpressionIdentifier("a")).getValue();
 NodeInfo b = context.jxPath("$[1]", new CoreExpressionIdentifier("b")).getValue();
 NodeInfo c = context.setContextItems(NodeInfo.createNodeInfo(new Integer(2))).jxPath("$[1]", new CoreExpressionIdentifier("c")).getValue();
 assertFalse(expression.computeValue(context, new Object[]{a, b}));
 assertTrue(expression.computeValue(context, new Object[]{a, c}));
 context.clearVariables();
 }

 @Test
 public void testCompareIterators() {
 List<Integer> listA = new ArrayList<>();
 listA.add(1);
 listA.add(2);
 listA.add(3);
 listA.add(4);
 List<Integer> listB = new ArrayList<>();
 listB.add(4);
 listB.add(3);
 listB.add(2);
 listB.add(1);
 Iterator<NodeInfo> itA = context.setContextItems(NodeInfo.createNodeInfos(listA.toArray())).jxPath("$[*]", new CoreExpressionIdentifier("a")).getIterators().next();
 Iterator<NodeInfo> itB = context.jxPath("$[*]", new CoreExpressionIdentifier("b")).getIterators().next();
 assertTrue(expression.computeValue(context, new Object[]{itA, itB}));
 context.clearVariables();
 itA = context.setContextItems(NodeInfo.createNodeInfos(listA.toArray())).jxPath("$[*]", new CoreExpressionIdentifier("a")).getIterators().next();
 itB = context.jxPath("$[*]", new CoreExpressionIdentifier("b")).getIterators().next();
 assertFalse(expression.computeValue(context, new Object[]{itA, itB}));
 context.clearVariables();
 itA = context.setContextItems(NodeInfo.createNodeInfos(listB.toArray())).jxPath("$[*]", new CoreExpressionIdentifier("b")).getIterators().next();
 itB = context.jxPath("$[*]", new CoreExpressionIdentifier("a")).getIterators().next();
 assertFalse(expression.computeValue(context, new Object[]{itA, itB}));
 context.clearVariables();
 }

 @Test
 public void testCompareCollectionAndIterator() {
 List<Integer> listA = new ArrayList<>();
 listA.add(1);
 listA.add(2);
 List<Integer> listB = new ArrayList<>();
 listB.add(2);
 listB.add(1);
 Iterator<NodeInfo> itB = context.jxPath("$[*]", new CoreExpressionIdentifier("b")).getIterators().next();
 assertTrue(expression.computeValue(context, new Object[]{NodeInfo.createNodeInfos(listA.toArray()), itB}));
 context.clearVariables();
 itB = context.jxPath("$[*]", new CoreExpressionIdentifier("b")).getIterators().next();
 assertFalse(expression.computeValue(context, new Object[]{NodeInfo.createNodeInfos(listA.toArray()), itB}));
 context.clearVariables();
 }

 @Test
 public void testCompareIteratorAndCollection() {
 List<Integer> listA = new ArrayList<>();
 listA.add(1);
 listA.add(2);
 listA.add(3);
 listA.add(4);
 Iterator<NodeInfo> itA = context.setContextItems(NodeInfo.createNodeInfos(listA.toArray())).jxPath("$[*]", new CoreExpressionIdentifier("a")).getIterators().next();
 List<Integer> listB = new ArrayList<>();
 listB.add(2);
 listB.add(1);
 assertTrue(expression.computeValue(context, new Object[]{itA, NodeInfo.createNodeInfos(listB.toArray())}));
 context.clearVariables();
 itA = context.setContextItems(NodeInfo.createNodeInfos(listA.toArray())).jxPath("$[*]", new CoreExpressionIdentifier("a")).getIterators().next();
 assertFalse(expression.computeValue(context, new Object[]{itA, NodeInfo.createNodeInfos(listB.toArray())}));
 context.clearVariables();
 }

 @Test
 public void testCompareCollections() {
 List<Integer> listA = new ArrayList<>();
 listA.add(1);
 listA.add(2);
 listA.add(3);
 listA.add(4);
 List<Integer> listB = new ArrayList<>();
 listB.add(3);
 listB.add(2);
 listB.add(1);
 List<Integer> listC = new ArrayList<>();
 listC.add(4);
 listC.add(3);
 listC.add(2);
 List<List<Integer>> collections = new ArrayList<>();
 collections.add(listA);
 collections.add(listB);
 collections.add(listC);
 collections.sort(new Comparator<List<Integer>>() {
 @Override
 public int compare(List<Integer> o1, List<Integer> o2) {
 return Collections.sort(o1, Collections.reverseOrder()).get(0).compareTo(Collections.sort(o2, Collections.reverseOrder()).get(0));
 }
 });
 NodeInfo left = NodeInfo.createNodeInfos(collections.get(0).toArray());
 NodeInfo right = NodeInfo.createNodeInfos(collections.get(1).toArray());
 assertTrue(expression.computeValue(context, new Object[]{left, right}));
 assertFalse(expression.computeValue(context, new Object[]{left, collections.get(2)}));
 context.clearVariables();
 }

 @Test
 public void testCompareNull() {
 NodeInfo left = null;
 NodeInfo right = NodeInfo.createNodeInfo(new Integer(3));
 assertFalse(expression.computeValue(context, new Object[]{left, right}));
 context.clearVariables();
 left = NodeInfo.createNodeInfo(new Integer(3));
 right = null;
 assertFalse(expression.computeValue(context, new Object[]{left, right}));
 context.clearVariables();
 }
}