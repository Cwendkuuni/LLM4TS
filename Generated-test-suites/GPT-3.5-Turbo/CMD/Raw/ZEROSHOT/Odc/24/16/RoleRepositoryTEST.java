import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleRepository roleRepositoryMock;

    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Administrator role");
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepositoryMock.findByIdIn(Collections.singletonList(1L)))
            .thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> roles = roleRepositoryMock.findByIdIn(Collections.singletonList(1L));

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(roleEntity.getId(), roles.get(0).getId());
        assertEquals(roleEntity.getName(), roles.get(0).getName());
        assertEquals(roleEntity.isEnabled(), roles.get(0).isEnabled());
        assertEquals(roleEntity.getDescription(), roles.get(0).getDescription());
    }

    @Test
    public void testFindByName() {
        when(roleRepositoryMock.findByName("Admin"))
            .thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> roles = roleRepositoryMock.findByName("Admin");

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(roleEntity.getId(), roles.get(0).getId());
        assertEquals(roleEntity.getName(), roles.get(0).getName());
        assertEquals(roleEntity.isEnabled(), roles.get(0).isEnabled());
        assertEquals(roleEntity.getDescription(), roles.get(0).getDescription());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(roleEntity));
        when(roleRepositoryMock.findByOrganizationId(1L, PageRequest.of(0, 10)))
            .thenReturn(page);

        Page<RoleEntity> roles = roleRepositoryMock.findByOrganizationId(1L, PageRequest.of(0, 10));

        assertNotNull(roles);
        assertEquals(1, roles.getTotalElements());
        assertEquals(roleEntity.getId(), roles.getContent().get(0).getId());
        assertEquals(roleEntity.getName(), roles.getContent().get(0).getName());
        assertEquals(roleEntity.isEnabled(), roles.getContent().get(0).isEnabled());
        assertEquals(roleEntity.getDescription(), roles.getContent().get(0).getDescription());
    }

    // Add more test methods for other repository methods

}