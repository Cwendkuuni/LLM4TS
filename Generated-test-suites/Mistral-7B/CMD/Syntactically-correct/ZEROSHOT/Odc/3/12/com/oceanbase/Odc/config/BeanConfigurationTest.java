package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandlerImpl;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpFirewall defaultHttpFirewall;

    @Autowired
    private CloudMetadataClient cloudMetadataClient;

    @Autowired
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Bean<PasswordEncoder> passwordEncoderBean = Mockito.mock(Bean.class);
        Mockito.when(passwordEncoderBean.getObject()).thenReturn(encoder);

        PasswordEncoder actual = BeanConfiguration.passwordEncoder();

        assertEquals(encoder, actual);
    }

    @Test
    public void testObjectMapperBean() {
        SensitivePropertyHandler sensitivePropertyHandlerMock = Mockito.mock(SensitivePropertyHandler.class);

        CustomOutputSerializer customOutputSerializer = Mockito.mock(CustomOutputSerializer.class);
        Mockito.when(customOutputSerializer.newSerializer(Internationalizable.class))
                .thenReturn(new I18nOutputSerializer());

        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        Mockito.when(objectMapperMock.registerModule(JacksonModules.sensitiveInputHandling(Mockito.any()))).thenReturn(objectMapperMock);
        Mockito.when(objectMapperMock.registerModule(JacksonModules.customOutputHandling(customOutputSerializer)))
                .thenReturn(objectMapperMock);

        ObjectMapper actual = BeanConfiguration.objectMapper(sensitivePropertyHandlerMock);

        assertEquals(objectMapperMock, actual);
    }

    @Test
    public void testDefaultHttpFirewallBean() throws JsonProcessingException {
        DefaultHttpFirewall defaultHttpFirewallMock = Mockito.mock(DefaultHttpFirewall.class);

        Mockito.when(defaultHttpFirewallMock.setAllowUrlEncodedSlash(true)).thenReturn(defaultHttpFirewallMock);

        HttpFirewall actual = BeanConfiguration.defaultHttpFirewall();

        assertEquals(defaultHttpFirewallMock, actual);
    }

    @Test
    public void testCloudMetadataClientBean() {
        CloudMetadataClient cloudMetadataClientMock = Mockito.mock(CloudMetadataClient.class);

        Bean<CloudMetadataClient> cloudMetadataClientBean = Mockito.mock(Bean.class);
        Mockito.when(cloudMetadataClientBean.getObject()).thenReturn(cloudMetadataClientMock);

        CloudMetadataClient actual = BeanConfiguration.cloudMetadataClient();

        assertEquals(cloudMetadataClientMock, actual);
    }

    @Test
    public void testCookieSerializerBean() {
        DefaultCookieSerializer serializerMock = Mockito.mock(DefaultCookieSerializer.class);
        Bean<CookieSerializer> cookieSerializerBean = Mockito.mock(Bean.class);
        Mockito.when(cookieSerializerBean.getObject()).thenReturn(serializerMock);

        CookieSerializer actual = BeanConfiguration.cookieSerializer();

        assertEquals(serializerMock, actual);
    }

    @Test
    public void testSensitivePropertyHandlerBean() {
        SensitivePropertyHandler actual = BeanConfiguration.sensitivePropertyHandler();
        assertTrue(actual instanceof SensitivePropertyHandlerImpl);
    }
}