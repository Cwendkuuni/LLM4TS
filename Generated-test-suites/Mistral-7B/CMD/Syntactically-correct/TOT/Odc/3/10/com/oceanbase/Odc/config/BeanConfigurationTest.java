import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
    }

    @Test
    public void testPasswordEncoderBeanCreation() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapperBeanCreation() {
        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
                        .addSerializer(Internationalizable.class, new I18nOutputSerializer())));
        ObjectMapper actualObjectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertEquals(expectedObjectMapper, actualObjectMapper);
    }

    @Test
    public void testDefaultHttpFirewallBeanCreation() {
        HttpFirewall expectedHttpFirewall = new DefaultHttpFirewall();
        expectedHttpFirewall.setAllowUrlEncodedSlash(true);
        HttpFirewall actualHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertEquals(expectedHttpFirewall, actualHttpFirewall);
    }

    @Test
    public void testCloudMetadataClientBeanCreation() {
        NullCloudMetadataClient expectedNullCloudMetadataClient = new NullCloudMetadataClient();
        NullCloudMetadataClient actualNullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertEquals(expectedNullCloudMetadataClient, actualNullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializerBeanCreation() {
        DefaultCookieSerializer expectedCookieSerializer = new DefaultCookieSerializer();
        DefaultCookieSerializer actualCookieSerializer = beanConfiguration.cookieSerializer();
        assertEquals(expectedCookieSerializer, actualCookieSerializer);
    }

    @Test
    public void testGetTenantEndpointFromNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenantEndpoint expectedTenantEndpoint = new OBTenantEndpoint();
        expectedTenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
        OBTenantEndpoint actualTenantEndpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertEquals(expectedTenantEndpoint, actualTenantEndpoint);
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstancesFromNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenantsFromNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantFromNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsersFromNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUserFromNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.getSysTenantUser("instanceId");
    }

    @Test
    public void testBeanConfigurationInitialization() {
        ArgumentCaptor<SensitivePropertyHandler> sensitivePropertyHandlerArgumentCaptor = ArgumentCaptor.forClass(SensitivePropertyHandler.class);
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(BeanConfiguration.class);
        applicationContext.refresh();
        BeanConfiguration beanConfiguration = applicationContext.getBean(BeanConfiguration.class);
        Mockito.verify(applicationContext).registerBean(SensitivePropertyHandler.class, Mockito.any());
        Mockito.verify(applicationContext).registerBean(ObjectMapper.class, sensitivePropertyHandlerArgumentCaptor.capture());
        ObjectMapper objectMapper = sensitivePropertyHandlerArgumentCaptor.getValue();
        assertEquals(beanConfiguration.objectMapper(sensitivePropertyHandler), objectMapper);
    }
}