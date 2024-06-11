
import org.junit.Test;

public class BeanConfigurationTest {
  
  @Test
  public void testPasswordEncoder() {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    assertNotNull(encoder);
  }

  @Test
  public void testObjectMapper() {
    SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
    ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
    assertNotNull(objectMapper);
  }

  @Test
  public void testDefaultHttpFirewall() {
    HttpFirewall httpFirewall = new BeanConfiguration().defaultHttpFirewall();
    assertNotNull(httpFirewall);
  }

  @Test
  public void testCloudMetadataClient() {
    CloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
    assertNotNull(cloudMetadataClient);
  }

  @Test
  public void testCookieSerializer() {
    CookieSerializer cookieSerializer = new BeanConfiguration().cookieSerializer();
    assertNotNull(cookieSerializer);
  }

  @Test
  public void testNullCloudMetadataClient() {
    NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
    assertNotNull(nullCloudMetadataClient);
  }

  // Additional Test Cases for NullCloudMetadataClient methods
  @Test(expected = UnsupportedException.class)
  public void testListInstances() {
    NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
    nullCloudMetadataClient.listInstances();
  }

  @Test(expected = UnsupportedException.class)
  public void testListTenants() {
    NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
    nullCloudMetadataClient.listTenants("instanceId");
  }

  @Test(expected = UnsupportedException.class)
  public void testGetTenant() {
    NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
    nullCloudMetadataClient.getTenant("instanceId", "tenantId");
  }

  @Test(expected = UnsupportedException.class)
  public void testListDatabaseUsers() {
    NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
    nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
  }

  @Test
  public void testGetTenantEndpoint() {
    NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
    OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
    assertNotNull(endpoint);
    assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
  }

  @Test(expected = UnsupportedException.class)
  public void testGetSysTenantUser() {
    NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
    nullCloudMetadataClient.getSysTenantUser("instanceId");
  }
}
