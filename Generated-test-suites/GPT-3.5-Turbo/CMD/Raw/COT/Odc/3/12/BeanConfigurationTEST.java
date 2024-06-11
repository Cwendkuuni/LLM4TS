
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(mock(SensitivePropertyHandler.class)));
    }

    // Add more basic test cases for other methods in BeanConfiguration here

    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        beanConfiguration.new NullCloudMetadataClient().listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenants() {
        beanConfiguration.new NullCloudMetadataClient().listTenants("instanceId");
    }

    // Add more test cases for other exceptions and edge cases here
}

