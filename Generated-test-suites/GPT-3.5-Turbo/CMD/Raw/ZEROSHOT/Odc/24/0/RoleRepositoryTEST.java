import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setup() {
        roleRepository = Mockito.mock(RoleRepository.class);
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByName() {
        String name = "admin";
        List<RoleEntity> roleEntities = new ArrayList<>();
        Mockito.when(roleRepository.findByName(name)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByName(name);

        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<RoleEntity> roles = new ArrayList<>();
        Page<RoleEntity> page = new PageImpl<>(roles);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(page, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "admin";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        Optional<RoleEntity> optional = Optional.of(roleEntity);
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optional);

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(result.isPresent());
        assertEquals(roleEntity, result.get());
    }

    // Add tests for other methods

}