import static org.junit.Assert.*;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {

    private EvalContext mockContext1;
    private EvalContext mockContext2;

    @Before
    public void setUp() {
        mockContext1 = new MockEvalContext();
        mockContext2 = new MockEvalContext();
    }

    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{mockContext1, mockContext2});
        int documentOrder = unionContext.getDocumentOrder();
        assertEquals(1, documentOrder);
    }

    @Test
    public void testSetPosition() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{mockContext1, mockContext2});
        boolean setPosition = unionContext.setPosition(1);
        assertEquals(true, setPosition);
    }

    private class MockEvalContext extends EvalContext {
        @Override
        public boolean nextSet() {
            return true;
        }

        @Override
        public boolean nextNode() {
            return true;
        }

        @Override
        public NodePointer getCurrentNodePointer() {
            return new MockNodePointer();
        }
    }

    private class MockNodePointer extends NodePointer {
        @Override
        public boolean isCollection() {
            return true;
        }
    }
}