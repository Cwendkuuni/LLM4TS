import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Extension
@Slf4j
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension extension;

    @BeforeEach
    public void setUp() {
        connection = mock(Connection.class);
        extension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        extension.killQuery(connection, "connectionId");
        Mockito.verify(connection.createStatement()).execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = "currentSchema";
        String schemaName = "newSchema";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);
        when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(schemaName);
        when(connection.update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName))).thenReturn(1);

        extension.switchSchema(connection, schemaName);
        Assertions.assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaWithCurrentSchemaNull() throws SQLException {
        String schemaName = "newSchema";
        when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(null);
        try {
            extension.switchSchema(connection, schemaName);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals("Cannot find the current schema.", e.getMessage());
        }
    }

    @Test
    public void testSwitchSchemaWithCurrentSchemaSameAsTarget() throws SQLException {
        String schemaName = "currentSchema";
        extension.switchSchema(connection, schemaName);
        Assertions.assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaWithUpdateException() throws SQLException {
        String schemaName = "newSchema";
        String currentSchema = "currentSchema";
        when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);
        when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(schemaName);
        when(connection.update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName))).thenThrow(new SQLException("Exception occurs."));

        try {
            extension.switchSchema(connection, schemaName);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals("Exception occurs.", e.getMessage());
        }
    }
}