import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext ctx1 = new EvalContext();
        EvalContext ctx2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{ctx1, ctx2});

        int expected = 0;
        int actual = unionContext.getDocumentOrder();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetPosition() {
        EvalContext ctx1 = new EvalContext();
        EvalContext ctx2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{ctx1, ctx2});

        int position = 1;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);

        assertEquals(expected, actual);
    }

    @Test
    public void testSetPosition_NotPrepared() {
        EvalContext ctx1 = new EvalContext();
        EvalContext ctx2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{ctx1, ctx2});

        int position = 1;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);

        assertEquals(expected, actual);
        //Assert for not prepared state
    }

    @Test
    public void testSetPosition_Prepared() {
        EvalContext ctx1 = new EvalContext();
        EvalContext ctx2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{ctx1, ctx2});

        unionContext.setPosition(1);

        int position = 2;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);

        assertEquals(expected, actual);
        //Assert for prepared state
    }

    @Test
    public void testSetPosition_Prepared_InvalidPosition() {
        EvalContext ctx1 = new EvalContext();
        EvalContext ctx2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{ctx1, ctx2});

        unionContext.setPosition(1);

        int position = 3;
        boolean expected = false;
        boolean actual = unionContext.setPosition(position);

        assertEquals(expected, actual);
        //Assert for prepared state with invalid position
    }

    
}