import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 @InjectMocks
 private BeanConfiguration beanConfiguration;

 @Before
 public void setUp() {
 OBInstance instance = new OBInstance();
 instance.setId("instance-1");

 OBTenant tenant = new OBTenant();
 tenant.setId("tenant-1");
 tenant.setName("tenant-name-1");

 when(sensitivePropertyHandler.decrypt("tenant-name-1")).thenReturn("decrypted-tenant-name-1");
 when(sensitivePropertyHandler.decrypt("instance-1")).thenReturn("decrypted-instance-1");

 when(beanConfiguration.cloudMetadataClient().listInstances()).thenReturn(java.util.Arrays.asList(instance));
 when(beanConfiguration.cloudMetadataClient().listTenants("instance-1")).thenReturn(java.util.Arrays.asList(tenant));
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);

 assertEquals("decrypted-tenant-name-1", objectMapper.writeValueAsString(tenant));
 }
}