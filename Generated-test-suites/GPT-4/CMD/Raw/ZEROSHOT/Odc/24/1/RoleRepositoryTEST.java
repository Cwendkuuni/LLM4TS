import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.oceanbase.odc.metadb.iam.RoleEntity;

@DataJpaTest
public class RoleRepositoryTest {
    
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void findByIdInTest(){
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertNotNull(roles);
    }

    @Test
    public void findByNameTest(){
        String name = "name";
        List<RoleEntity> roles = roleRepository.findByName(name);
        assertNotNull(roles);
    }

    @Test
    public void findByOrganizationIdTest(){
        Long organizationId = 1L;
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, PageRequest.of(0,5));
        assertNotNull(roles);
    }

    @Test
    public void findByNameAndOrganizationIdTest(){
        String name = "name";
        Long organizationId = 1L;
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(role.isPresent());
    }

    @Test
    public void findByUserIdTest(){
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserId(userId);
        assertNotNull(roles);
    }

    @Test
    public void findByUserIdAndEnabledTest(){
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, true);
        assertNotNull(roles);
    }

    @Test
    public void findByRoleIdsAndEnabledTest(){
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertNotNull(roles);
    }

    @Test
    public void updateTest(){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("test");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("desc");
        roleEntity.setId(1L);
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}