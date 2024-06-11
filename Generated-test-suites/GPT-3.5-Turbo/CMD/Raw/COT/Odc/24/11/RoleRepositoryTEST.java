import com.oceanbase.odc.metadb.iam.RoleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import javax.persistence.EntityManager;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private EntityManager entityManager;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);

        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
        assertEquals(3, roleEntities.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = roleRepository.findByName("Admin");

        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
        assertEquals("Admin", roleEntities.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = mock(Pageable.class);
        Page<RoleEntity> roleEntityPage = roleRepository.findByOrganizationId(organizationId, pageable);

        assertNotNull(roleEntityPage);
        assertEquals(1L, roleEntityPage.getTotalElements());
    }

    // Similarly, write test cases for other methods with edge cases and exception handling scenarios

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("SuperAdmin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Super admin role");

        int updatedRows = roleRepository.update(roleEntity);

        assertEquals(1, updatedRows);
    }

}