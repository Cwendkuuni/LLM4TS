
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {
    private final String TEST_USERNAME = "test";
    private final String TEST_PASSWORD = "test123";

    private SingleConnectionDataSource dataSource;

    @Before
    public void initializeDatabase() {
        dataSource = Mockito.mock(SingleConnectionDataSource.class);

        when(dataSource.getUsername()).thenReturn(TEST_USERNAME);
        when(dataSource.getPassword()).thenReturn(TEST_PASSWORD);
    }

    @Test
    public void testGetUsername() {
        String result = dataSource.getUsername();
        assertTrue("Username should be '" + TEST_USERNAME + "'.", result.equals(TEST_USERNAME));
    }

    @Test
    public void testGetPassword() {
        String result = dataSource.getPassword();
        assertTrue("Password should be '" + TEST_PASSWORD + "'.", result.equals(TEST_PASSWORD));
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_InvalidUsernameAndPassword() throws SQLException {
        String invalidUsername = "username";
        String invalidPassword = "password";

        dataSource.getConnection(invalidUsername, invalidPassword);
    }

    @Test
    public void testValidUsernameAndPassword() {
        try {
            dataSource.getConnection(TEST_USERNAME, TEST_PASSWORD);
        } catch (SQLException e) {
            fail("Should not have thrown any exception");
        }
    }

    // Add additional tests methods for all other public methods including edge cases
}

