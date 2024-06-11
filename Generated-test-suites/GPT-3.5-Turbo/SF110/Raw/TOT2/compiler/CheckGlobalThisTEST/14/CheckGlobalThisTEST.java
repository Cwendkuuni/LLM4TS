import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        
        Node node = new Node(Token.THIS);
        assertTrue(checkGlobalThis.shouldReportThis(node));
        
        // Add more test cases here for different scenarios
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        
        Node node = new Node(Token.NAME, new Node(Token.FUNCTION));
        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
        
        // Add assertions to test jsDocInfo values
    }

    // Add more test cases for other public methods in CheckGlobalThis class

}