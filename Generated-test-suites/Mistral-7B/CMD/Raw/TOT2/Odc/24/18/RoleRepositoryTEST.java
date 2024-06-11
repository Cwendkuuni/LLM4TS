import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RoleRepositoryTest {

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedRoleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(expectedRoleEntities, roleEntities);
    }
}