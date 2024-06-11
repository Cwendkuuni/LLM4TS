import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.EvalContext;
import java.util.ArrayList;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
    
    private UnionContext unionContext;
    
    @Before
    public void setUp() {
        EvalContext[] contexts = new EvalContext[]{new EvalContext(), new EvalContext()};
        unionContext = new UnionContext(new EvalContext(), contexts);
    }
    
    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }
    
    @Test
    public void testSetPosition() {
        assertTrue(unionContext.setPosition(0));
    }
    
    @Test
    public void testSetPositionBeforePreparation() {
        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPositionOutOfBounds() {
        assertFalse(unionContext.setPosition(5));
    }

    @Test(expected = NullPointerException.class)
    public void testSetPositionNullPointer() {
        unionContext = new UnionContext(new EvalContext(), null);
        unionContext.setPosition(0);
    }
    
    @Test
    public void testSetPositionEmptyContexts() {
        unionContext = new UnionContext(new EvalContext(), new EvalContext[]{});
        assertFalse(unionContext.setPosition(0));
    }
}