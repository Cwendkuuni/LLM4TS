
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testGetCurrentNodePointer() {
        AttributeContext context = new AttributeContext(null, null);
        assert context.getCurrentNodePointer() == null;
    }

    @Test
    public void testReset() {
        AttributeContext context = new AttributeContext(null, null);
        context.setStarted = true;
        context.iterator = new MockNodeIterator();
        context.currentNodePointer = new MockNodePointer();
        
        context.reset();
        
        assert !context.setStarted;
        assert context.iterator == null;
        assert context.currentNodePointer == null;
    }

    @Test
    public void testSetPosition() {
        MockEvalContext parentContext = new MockEvalContext(null);
        parentContext.currentNodePointer = new MockNodePointer();
        
        AttributeContext context = new AttributeContext(parentContext, null);
        
        context.setPosition(2);
        
        assert context.getCurrentPosition() == 2;
        assert context.iterator.getPosition() == 2;
    }

    @Test
    public void testNextNode() {
        MockEvalContext parentContext = new MockEvalContext(null);
        parentContext.currentNodePointer = new MockNodePointer();
        parentContext.currentNodePointer.nodePointer = new MockNodePointer();
        
        AttributeContext context = new AttributeContext(parentContext, new MockNodeTest());
        context.setStarted = false;
        
        assert context.nextNode();
        assert context.getCurrentPosition() == 1;
        assert context.currentNodePointer == parentContext.currentNodePointer.nodePointer;
    }

    @Test
    public void testNextNodeFalse() {
        MockEvalContext parentContext = new MockEvalContext(null);
        
        AttributeContext context = new AttributeContext(parentContext, new MockNodeTest());
        context.setStarted = false;
        
        assert !context.nextNode();
    }

    @Test
    public void testNextNodeFalseWithNullIterator() {
        MockEvalContext parentContext = new MockEvalContext(null);
        
        AttributeContext context = new AttributeContext(parentContext, new MockNodeTest());
        context.setStarted = true;
        context.iterator = null;
        
        assert !context.nextNode();
    }
    
    private static class MockEvalContext extends EvalContext {
        private NodePointer currentNodePointer;
        
        MockEvalContext(EvalContext parentContext) {
            super(parentContext);
        }
        
        @Override
        public NodePointer getCurrentNodePointer() {
            return currentNodePointer;
        }
        
        @Override
        public NodeIterator attributeIterator(QName name) {
            return new MockNodeIterator();
        }
    }

    private static class MockNodeIterator implements NodeIterator {
        private int position;
        
        @Override
        public boolean setPosition(int position) {
            this.position = position;
            return true;
        }
        
        @Override
        public int getPosition() {
            return position;
        }
        
        @Override
        public NodePointer getNodePointer() {
            return new MockNodePointer();
        }
    }

    private static class MockNodePointer extends NodePointer {
        @Override
        public NodePointer getAttribute(QName name) {
            return new MockNodePointer();
        }
    }
    
    private static class MockNodeTest extends NodeTest {
    }
}