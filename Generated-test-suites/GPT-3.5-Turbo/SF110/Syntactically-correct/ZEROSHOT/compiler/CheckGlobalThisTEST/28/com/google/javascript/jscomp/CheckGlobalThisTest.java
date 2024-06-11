import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(null);
    }
    
    @Test
    public void testShouldReportThis_noParent_returnFalse() {
        assertFalse(checkGlobalThis.shouldReportThis(new Node()));
    }
    
    @Test
    public void testShouldReportThis_assignLhsChildNotNull_returnTrue() {
        checkGlobalThis.assignLhsChild = new Node();
        assertTrue(checkGlobalThis.shouldReportThis(new Node()));
    }
    
    @Test
    public void testShouldReportThis_parentIsGet_returnTrue() {
        Node parent = new Node();
        parent.setType(Token.GET);
        assertTrue(checkGlobalThis.shouldReportThis(parent));
    }
    
    @Test
    public void testShouldReportThis_parentNotGet_returnFalse() {
        assertFalse(checkGlobalThis.shouldReportThis(new Node()));
    }
}