import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 private NullCloudMetadataClient cloudMetadataClient;
 @Autowired
 @Qualifier("cloudMetadataClient")
 void setCloudMetadataClient(NullCloudMetadataClient cloudMetadataClient) {
 this.cloudMetadataClient = cloudMetadataClient;
 }

 @Before
 public void initMocks() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCloudMetadataClientIsNotNull() {
 assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testCloudMetadataClientSupportsCloudMetadataReturnsFalse() {
 assertFalse(cloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testCloudMetadataClientSupportsTenantInstanceReturnsFalse() {
 assertFalse(cloudMetadataClient.supportsTenantInstance());
 }

 @Test
 public void testCloudMetadataClientNeedsOBTenantNameReturnsFalse() {
 assertFalse(cloudMetadataClient.needsOBTenantName());
 }

 @Test
 public void testCloudMetadataClientNeedsSysTenantUserReturnsFalse() {
 assertFalse(cloudMetadataClient.needsSysTenantUser());
 }

 @Test
 public void testCloudMetadataClientIncludeClusterNameForJdbcUsernameReturnsTrue() {
 assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
 }

 @Test
 public void testCloudMetadataClientOceanbaseAccessModeReturnsDIRECT() {
 assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
 }

 @Test(expected = UnsupportedException.class)
 public void testListInstancesThrowsUnsupportedException() {
 cloudMetadataClient.listInstances();
 }

 @Test(expected = UnsupportedException.class)
 public void testListTenantsThrowsUnsupportedException() {
 cloudMetadataClient.listTenants("instanceId");
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantThrowsUnsupportedException() {
 cloudMetadataClient.getTenant("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testListDatabaseUsersThrowsUnsupportedException() {
 cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantEndpointThrowsUnsupportedException() {
 cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testGetSysTenantUserThrowsUnsupportedException() {
 cloudMetadataClient.getSysTenantUser("instanceId");
 }
}