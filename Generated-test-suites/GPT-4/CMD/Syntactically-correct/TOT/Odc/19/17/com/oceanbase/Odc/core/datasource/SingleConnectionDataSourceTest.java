package com.oceanbase.odc.core.datasource;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource singleConnectionDataSource;
    private String username = "testUser";
    private String password = "testPass";

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setUsername(username);
        singleConnectionDataSource.setPassword(password);
    }

    @Test
    public void whenNewConnection_thenNotNull() {
        try {
            Connection connection = singleConnectionDataSource.getConnection();
            assertThat(connection, is(not(nullValue())));
        } catch (SQLException e) {
            fail("Unexpected SQL Exception: " + e.getMessage());
        }
    }

    @Test
    public void whenValidUserInfo_thenSuccess() {
        try {
            Connection connection = singleConnectionDataSource.getConnection(username, password);
            assertThat(connection, is(not(nullValue())));
        } catch (SQLException e) {
            fail("Unexpected SQL Exception: " + e.getMessage());
        }
    }

    @Test(expected = SQLException.class)
    public void whenInvalidUserInfo_thenSQLException() throws SQLException {
        singleConnectionDataSource.getConnection("invalidUser", "invalidPass");
    }
}