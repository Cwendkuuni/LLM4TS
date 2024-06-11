import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(Collections.singletonList(1L));
        assertNotNull(roleEntities);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = roleRepository.findByName("Admin");
        assertNotNull(roleEntities);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, Pageable.unpaged());
        assertNotNull(roleEntities);
        assertEquals(2, roleEntities.getContent().size());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertNotNull(roleEntity);
        assertEquals("Admin", roleEntity.get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Admin", "User"));
        assertNotNull(roleEntities);
        assertEquals(2, roleEntities.size());
    }

    // Add more test cases for remaining methods

}