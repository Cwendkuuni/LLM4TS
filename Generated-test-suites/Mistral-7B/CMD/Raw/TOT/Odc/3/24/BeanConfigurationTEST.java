import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;
 private NullCloudMetadataClient cloudMetadataClient;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 beanConfiguration = new BeanConfiguration();
 cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 }

 @Test
 public void testCloudMetadataClient() {
 // Test null instance
 assertFalse(cloudMetadataClient.supportsCloudMetadata());
 // Test empty instance id
 assertFalse(cloudMetadataClient.supportsTenantInstance());
 // Test tenant name is null
 assertFalse(cloudMetadataClient.needsSysTenantUser());
 // Test tenant name is not null
 assertTrue(cloudMetadataClient.needsSysTenantUser());
 // Test includes cluster name for JDBC username
 assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
 // Test oceanbase access mode
 assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
 // Test list instances
 assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
 // Test list tenants
 assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants("instanceId"));
 // Test get tenant
 assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("instanceId", "tenantId"));
 // Test list database users
 assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
 // Test get tenant endpoint
 OBTenantEndpoint expected = new OBTenantEndpoint();
 expected.setAccessMode(OceanBaseAccessMode.DIRECT);
 OBTenantEndpoint actual = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 assertEquals(expected, actual);
 }
}