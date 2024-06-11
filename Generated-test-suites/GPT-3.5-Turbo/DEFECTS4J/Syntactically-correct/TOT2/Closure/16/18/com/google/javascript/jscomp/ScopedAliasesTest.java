import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ScopedAliasesTest {

    @Test
    public void testScopedAliases() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);

        // Test for proper processing of goog.scope block with defined alias
        // var x = goog.y;
        // x.functionCall();
        assertEquals("goog.y.functionCall()", scopedAliases.processAlias("x.functionCall()"));

        // Test for redefinition error in goog.scope block
        try {
            scopedAliases.processAlias("var x = goog.y; var x = goog.z;");
            fail("Expected error for duplicate alias definition");
        } catch (Exception e) {
            // Expected exception
        }

        // Test for using return in goog.scope block
        try {
            scopedAliases.processAlias("goog.x = function() { return 2; };");
            fail("Expected error for using return in goog.scope function");
        } catch (Exception e) {
            // Expected exception
        }

        // Test for using this in goog.scope block
        try {
            scopedAliases.processAlias("goog.x = function() { var self = this; };");
            fail("Expected error for using this in goog.scope function");
        } catch (Exception e) {
            // Expected exception
        }

        // Test for using throw in goog.scope block
        try {
            scopedAliases.processAlias("goog.x = function() { throw 'Error'; };");
            fail("Expected error for using throw in goog.scope function");
        } catch (Exception e) {
            // Expected exception
        }
    }
}