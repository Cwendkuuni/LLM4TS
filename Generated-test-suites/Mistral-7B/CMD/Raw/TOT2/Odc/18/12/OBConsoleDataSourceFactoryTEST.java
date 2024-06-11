package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory testSubject;
 private EventPublisher eventPublisherMock;
 private ConnectionExtensionPoint connectionExtensionPointMock;
 private ConnectionConfig connectionConfigMock;

 @Before
 public void setUp() {
 this.eventPublisherMock = Mockito.mock(EventPublisher.class);
 this.connectionExtensionPointMock = Mockito.mock(ConnectionExtensionPoint.class);
 this.connectionConfigMock = Mockito.mock(ConnectionConfig.class);

 ConnectionInitializer connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
 when(connectionExtensionPointMock.getConnectionInitializers()).thenReturn(new ConnectionInitializer[] { connectionInitializerMock });

 this.testSubject = new OBConsoleDataSourceFactory(this.connectionConfigMock, true, true);
 }

 @After
 public void tearDown() {
 this.testSubject = null;
 this.connectionConfigMock = null;
 this.connectionExtensionPointMock = null;
 this.eventPublisherMock = null;
 }

 @Test
 public void testGetDataSource() throws SQLException {
 // Given
 DataSource expectedDataSource = new BasicDataSource();
 when(this.connectionExtensionPointMock.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
 when(this.connectionExtensionPointMock.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap())).thenReturn("jdbc:mysql://localhost:3306/test?useSSL=false&trustServerCertificate=true");
 when(this.connectionExtensionPointMock.getConnectionInitializers()).thenReturn(new ConnectionInitializer[] {});

 // When
 DataSource actualDataSource = this.testSubject.getDataSource();

 // Then
 assertEquals(expectedDataSource, actualDataSource);

 // Verify
 verify(this.connectionExtensionPointMock, times(3)).getDriverClassName();
 verify(this.connectionExtensionPointMock, times(1)).getConnectionInitializers();
 verifyZeroInteractions(this.connectionExtensionPointMock, this.eventPublisherMock, this.connectionConfigMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullConnectionConfig() {
 this.testSubject = new OBConsoleDataSourceFactory(null, true, true);
 }

 @Test
 public void testGetJdbcUrl() {
 // Given
 OBTenantEndpoint endpointMock = Mockito.mock(OBTenantEndpoint.class);
 SSLConfig sslConfigMock = Mockito.mock(SSLConfig.class);
 SSLFileEntry sslFileEntryMock = Mockito.mock(SSLFileEntry.class);
 when(this.connectionConfigMock.getHost()).thenReturn("localhost");
 when(this.connectionConfigMock.getPort()).thenReturn(3306);
 when(this.connectionConfigMock.getDefaultSchema()).thenReturn("test");
 when(this.connectionConfigMock.getDialectType()).thenReturn(DialectType.OB_MYSQL);
 when(this.connectionConfigMock.getTenantName()).thenReturn("tenantName");
 when(this.connectionConfigMock.getOBTenantName()).thenReturn("obTenantName");
 when(this.connectionConfigMock.getClusterName()).thenReturn("clusterName");
 when(this.connectionConfigMock.getEndpoint()).thenReturn(endpointMock);
 when(this.connectionConfigMock.getSslConfig()).thenReturn(sslConfigMock);
 when(sslConfigMock.getEnabled()).thenReturn(true);
 when(sslConfigMock.getTrustServerCertificate()).thenReturn(true);
 when(sslFileEntryMock.getKeyStoreFilePath()).thenReturn("/path/to/keystore.p12");
 when(sslFileEntryMock.getKeyStoreFilePassword()).thenReturn("password".toCharArray());
 when(endpointMock.getProxyHost()).thenReturn("proxyHost");
 when(endpointMock.getProxyPort()).thenReturn(8080);
 when(this.connectionConfigMock.getJdbcUrlParameters()).thenReturn(new HashMap<>());

 // When
 String expectedJdbcUrl = "jdbc:mysql://localhost:3306/tenantName@obTenantName#clusterName@proxyHost:8080/test?useSSL=true&trustServerCertificate=true&keyStore=/path/to/keystore.p12&keyStorePassword=password";
 String actualJdbcUrl = this.testSubject.getJdbcUrl();

 // Then
 assertEquals(expectedJdbcUrl, actualJdbcUrl);

 // Verify
 verify(this.connectionConfigMock, times(13)).getHost();
 verify(this.connectionConfigMock, times(2)).getDefaultSchema();
 verify(this.connectionConfigMock, times(1)).getDialectType();
 verify(this.connectionConfigMock, times(1)).getTenantName();
 verify(this.connectionConfigMock, times(1)).getOBTenantName();
 verify(this.connectionConfigMock, times(1)).getClusterName();
 verify(this.connectionConfigMock, times(1)).getEndpoint();
 verify(this.connectionConfigMock, times(1)).getSslConfig();
 verify(sslConfigMock, times(1)).getEnabled();
 verify(sslConfigMock, times(1)).getTrustServerCertificate();
 verify(sslFileEntryMock, times(1)).getKeyStoreFilePath();
 verify(sslFileEntryMock, times(1)).getKeyStoreFilePassword();
 verify(endpointMock, times(1)).getProxyHost();
 verify(endpointMock, times(1)).getProxyPort();
 verify(this.connectionConfigMock, times(1)).getJdbcUrlParameters();
 }

}