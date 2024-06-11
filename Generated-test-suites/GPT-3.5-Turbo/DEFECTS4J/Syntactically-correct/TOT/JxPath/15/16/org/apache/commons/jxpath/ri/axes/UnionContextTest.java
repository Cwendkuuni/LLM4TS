import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

  @Test
  public void testGetDocumentOrder() {
    // Test with empty context array
    EvalContext[] emptyContexts = {};
    UnionContext emptyUnionContext = new UnionContext(null, emptyContexts);
    assertEquals(1, emptyUnionContext.getDocumentOrder());

    // Test with single context
    EvalContext[] singleContext = {new EvalContext()};
    UnionContext singleUnionContext = new UnionContext(null, singleContext);
    assertEquals(1, singleUnionContext.getDocumentOrder());

    // Test with multiple contexts
    EvalContext[] multipleContexts = {new EvalContext(), new EvalContext()};
    UnionContext multipleUnionContext = new UnionContext(null, multipleContexts);
    assertEquals(1, multipleUnionContext.getDocumentOrder());
  }

  @Test
  public void testSetPosition() {
    // Test with empty context array
    EvalContext[] emptyContexts = {};
    UnionContext emptyUnionContext = new UnionContext(null, emptyContexts);
    assertFalse(emptyUnionContext.setPosition(1));

    // Test with single context having no nodes
    EvalContext singleContextNoNode = new EvalContext();
    EvalContext[] singleContext = {singleContextNoNode};
    UnionContext singleUnionContextNoNode = new UnionContext(null, singleContext);
    assertFalse(singleUnionContextNoNode.setPosition(1));

    // Test with single context having nodes
    singleContextNoNode.setNodeSet(new BasicNodeSet());
    EvalContext singleContextWithNode = new EvalContext();
    singleContextWithNode.setNodeSet(new BasicNodeSet());
    NodePointer nodePointer = new NodePointer();
    singleContextWithNode.setCurrentNodePointer(nodePointer);
    EvalContext[] singleContextWithManyNodes = {singleContextNoNode, singleContextWithNode};
    UnionContext singleUnionContextWithNodes = new UnionContext(null, singleContextWithManyNodes);
    assertFalse(singleUnionContextWithNodes.setPosition(5));

    // Test with multiple contexts having nodes
    EvalContext multipleContextNoNode = new EvalContext();
    multipleContextNoNode.setNodeSet(new BasicNodeSet());
    EvalContext multipleContextWithNode = new EvalContext();
    multipleContextWithNode.setNodeSet(new BasicNodeSet());
    multipleContextWithNode.setCurrentNodePointer(nodePointer);
    EvalContext[] multipleContexts = {multipleContextNoNode, multipleContextWithNode};
    UnionContext multipleUnionContext = new UnionContext(null, multipleContexts);
    assertTrue(multipleUnionContext.setPosition(1));
    assertFalse(multipleUnionContext.setPosition(5));
  }

}