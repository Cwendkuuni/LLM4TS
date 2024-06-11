import com.oceanbase.odc.metadb.iam.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = List.of(1L, 2L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        Assertions.assertNotNull(roleEntities);
    }
}