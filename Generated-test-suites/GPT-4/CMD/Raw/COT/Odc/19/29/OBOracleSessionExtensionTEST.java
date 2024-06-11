import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Matchers.anyString;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension OBExtensionUnderTest;
    private Connection connectionMock;
    private JdbcTemplate jdbcTemplateMock;

    @Before
    public void setUp() throws Exception {
        OBExtensionUnderTest = new OBOracleSessionExtension();
        connectionMock = Mockito.mock(Connection.class);
        jdbcTemplateMock = Mockito.mock(JdbcTemplate.class);

        Mockito.when(JdbcOperationsUtil.getJdbcOperations(Mockito.any())).thenReturn(jdbcTemplateMock);
    }

    @Test
    public void testKillQuery() {
        OBExtensionUnderTest.killQuery(connectionMock, "1234");
        Mockito.verify(jdbcTemplateMock).execute("KILL QUERY 1234");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBExtensionUnderTest.switchSchema(connectionMock, "new_schema");
        Mockito.verify(jdbcTemplateMock).update("ALTER SESSION SET CURRENT_SCHEMA='new_schema'");
    }
    
    @Test
    public void testSwitchSameSchema() throws SQLException {
        Mockito.when(jdbcTemplateMock.queryForObject(anyString(), Mockito.<Class<String>>any())).thenReturn("same_schema");
        OBExtensionUnderTest.switchSchema(connectionMock, "same_schema");
        Mockito.verify(jdbcTemplateMock, Mockito.never()).update(Mockito.anyString());
    }    

    @Test
    public void testGetCurrentSchema() {
        OBExtensionUnderTest.getCurrentSchema(connectionMock);
        Mockito.verify(jdbcTemplateMock).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId() {
        OBExtensionUnderTest.getConnectionId(connectionMock);
        Mockito.verify(jdbcTemplateMock).queryForObject("select userenv('sessionid') from dual", String.class);
    }

    @Test
    public void testGetConnectionIdException() {
        OceanBaseConnection oceanBaseConnectionMock = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(jdbcTemplateMock.queryForObject(anyString(), Mockito.<Class<String>>any())).thenThrow(new RuntimeException());

        Mockito.when(oceanBaseConnectionMock.getServerThreadId()).thenReturn(1234L);

        Assert.assertEquals("1234", OBExtensionUnderTest.getConnectionId(oceanBaseConnectionMock));
    }
}