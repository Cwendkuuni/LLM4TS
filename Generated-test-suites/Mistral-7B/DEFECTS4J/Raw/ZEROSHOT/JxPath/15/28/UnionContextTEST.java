import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class UnionContextTest {

 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext parentContext;
 private UnionContext unionContext;
 private int position;

 @Parameterized.Parameters(name = "Test case {index}: contextA={0}, contextB={1}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 // Add test data here
 {new BasicNodeSet(), new BasicNodeSet(), 0},
 {new BasicNodeSet(new NodePointer(1)), new BasicNodeSet(new NodePointer(2)), 0},
 {new BasicNodeSet(new NodePointer(1)), new BasicNodeSet(new NodePointer(2)), 1},
 {new BasicNodeSet(new NodePointer(1)), new BasicNodeSet(new NodePointer(1)), 0},
 });
 }

 @Before
 public void setUp() throws Exception {
 contextA = new EvalContext(new BasicNodeSet(), null);
 contextB = new EvalContext(new BasicNodeSet(), null);
 parentContext = new EvalContext(new BasicNodeSet(), null);
 unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});
 }

 @Test
 public void testGetDocumentOrder() {
 assertThat(unionContext.getDocumentOrder(), is(1));
 }

 @Test
 public void testSetPosition_whenBothContextsHaveNodes_thenReturnsTrue() {
 for (int i = 0; i < contextA.size(); i++) {
 contextA.nextNode();
 contextB.nextNode();
 }
 position = 0;
 unionContext.nextSet();
 unionContext.nextNode();

 assertThat(unionContext.setPosition(position), is(true));
 }

 @Test
 public void testSetPosition_whenBothContextsHaveNodes_thenReturnsCurrentNodeFromFirstContext() {
 for (int i = 0; i < contextA.size(); i++) {
 contextA.nextNode();
 contextB.nextNode();
 }
 position = 0;
 unionContext.nextSet();
 unionContext.nextNode();

 NodePointer expectedNode = contextA.getCurrentNodePointer();
 NodePointer actualNode = unionContext.getCurrentNodePointer();

 assertThat(expectedNode, equalTo(actualNode));
 }

 @Test
 public void testSetPosition_whenFirstContextHasNoMoreNodes_thenReturnsNextNodeFromSecondContext() {
 for (int i = 0; i < contextA.size(); i++) {
 contextA.nextNode();
 }
 position = contextB.size();
 unionContext.nextSet();
 unionContext.nextNode();

 NodePointer expectedNode = contextB.getCurrentNodePointer();
 NodePointer actualNode = unionContext.getCurrentNodePointer();

 List<NodePointer> nodePointers = new ArrayList<>();
 unionContext.nextSet();
 while (unionContext.nextNode() != null) {
 nodePointers.add(unionContext.getCurrentNodePointer());
 }

 assertThat(expectedNode, equalTo(nodePointers.get(0)));
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPosition_whenFirstContextHasNoMoreNodes_thenThrowsException_whenPositionIsTooBig() {
 for (int i = 0; i < contextA.size(); i++) {
 contextA.nextNode();
 }
 position = contextB.size() + 1;
 unionContext.nextSet();
 unionContext.nextNode();
 }

 @Test
 public void testSetPosition_whenFirstContextHasNoMoreNodes_thenSetsPositionToFirstNodeOfSecondContext() {
 for (int i = 0; i < contextA.size(); i++) {
 contextA.nextNode();
 }
 position = contextB.size();
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextNode();

 NodePointer expectedNode = contextB.getCurrentNodePointer();
 NodePointer actualNode = unionContext.getCurrentNodePointer();

 assertThat(expectedNode, equalTo(actualNode));
 }
}