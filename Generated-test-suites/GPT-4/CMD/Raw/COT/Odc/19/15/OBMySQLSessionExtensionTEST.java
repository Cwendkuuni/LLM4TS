import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }
    
    @Test
    public void testKillQuery() {
        //TODO: Implement
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        //TODO: Implement
    }

    @Test
    public void testGetCurrentSchema() {
        //TODO: Implement
    }

    @Test
    public void testGetConnectionId() {
        //TODO: Implement
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKillQueryNull() {
        //TODO: Implement
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaNonExistent() {
        //TODO: Implement
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCurrentSchemaInvalidCon() {
        //TODO: Implement
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetConnectionIdInvalidCon() {
        //TODO: Implement
    }
}