package com.oceanbase.odc.plugin.connect.oboracle;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(this.connection, "conId");
        Mockito.verify(jdbcOperationsUtil, Mockito.times(1)).execute(any(String.class));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obOracleSessionExtension.switchSchema(this.connection, "schema");
        Mockito.verify(jdbcOperationsUtil, Mockito.times(1)).update(any(String.class));
    }

    @Test
    public void testGetCurrentSchema() {
        obOracleSessionExtension.getCurrentSchema(this.connection);
        Mockito.verify(jdbcOperationsUtil, Mockito.times(1)).queryForObject(any(String.class), any(Class.class));
    }

    @Test
    public void testGetConnectionId() {
        obOracleSessionExtension.getConnectionId(this.connection);
        Mockito.verify(jdbcOperationsUtil, Mockito.times(1)).queryForObject(any(String.class), any(Class.class));
    }

    @Test
    public void testGetConnectionIdException() {
        Mockito.when(jdbcOperationsUtil.queryForObject(any(String.class), any(Class.class))).thenThrow(new RuntimeException());
        Mockito.when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        obOracleSessionExtension.getConnectionId(oceanBaseConnection);
    }
}