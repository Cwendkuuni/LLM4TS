
import static org.junit.Assert.*;
import org.junit.Test;

public class SqlCheckServiceTest {

    @Test
    public void testCheck() {
        // Test check method with valid session and request
        // Verify that it returns a list of check results
    }

    @Test
    public void testCheckEnvironment() {
        // Test check method with environmentId, databaseName, sqls, and config
        // Verify that it returns a list of check violations
    }

    @Test
    public void testGetRules() {
        // Test getRules method with a list of rules and session
        // Verify that it returns a list of sql check rules
    }

    @Test
    public void testFullFillRiskLevel() {
        // Test fullFillRiskLevel method with a list of rules and violations
        // Verify that it assigns risk levels to violations based on rules
    }
}
