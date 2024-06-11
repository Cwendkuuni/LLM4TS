
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.oceanbase.odc.config.BeanConfiguration;

public class BeanConfigurationTest {

  @Test
  public void testPasswordEncoder() {
    BeanConfiguration config = new BeanConfiguration();
    PasswordEncoder encoder = config.passwordEncoder();
    assertNotNull(encoder);
  }
}
