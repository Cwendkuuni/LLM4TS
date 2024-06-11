package com.oceanbase.odc.core.datasource;
import static org.junit.Assert.*;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

@Test
public void defaultConstructorTest() {
	SingleConnectionDataSource datasource = new SingleConnectionDataSource();
	assertNotNull(datasource);
}

@Test
public void autoReconnectConstructorTest() {
	SingleConnectionDataSource datasource = new SingleConnectionDataSource(true);
	assertNotNull(datasource);
	assertTrue(datasource.getAutoReconnect());
}

@Test
public void getConnectionFirstTimeTest() throws SQLException {
	SingleConnectionDataSource datasource = new SingleConnectionDataSource();
	Connection connection = datasource.getConnection();
	assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void getConnectionAfterCloseTest() throws SQLException {
	SingleConnectionDataSource datasource = new SingleConnectionDataSource();
	datasource.close();
	datasource.getConnection();
}

@Test
public void getConnectionWithValidCredentials() throws SQLException {
	SingleConnectionDataSource datasource = new SingleConnectionDataSource();
	datasource.setUsername("test");
	datasource.setPassword("test");
	Connection connection = datasource.getConnection("test", "test");
	assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void getConnectionWithInvalidCredentials() throws SQLException {
	SingleConnectionDataSource datasource = new SingleConnectionDataSource();
	datasource.setUsername("test");
	datasource.setPassword("test");
	datasource.getConnection("wrong", "wrong");
}

@Test
public void connectionInitilization() {
	SingleConnectionDataSource datasource = new SingleConnectionDataSource();
	datasource.addInitializer(new ConnectionInitializer() {	@Override public void init(Connection con) throws SQLException { /* init logic here */ } });
	assertNotNull(datasource);
    //You could also assert this connection is correctly initialised
}

@Test(expected = SQLException.class)
public void resetConnectionTest() throws SQLException {
	SingleConnectionDataSource datasource = new SingleConnectionDataSource();
	datasource.resetConnection();
}

}