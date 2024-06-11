
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RoleRepositoryTest {

    private static final Collection<Long> ID_COLLECTION = Arrays.asList(1L, 2L);
    private static final Long ORGANIZATION_ID = 10L;
    private static final Long USER_ID = 5L;
    private static final Boolean IS_ENABLED = true;

    @Mock
    private RoleEntity roleEntity;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Pageable pageable;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(ID_COLLECTION);
        verify(roleRepository).findByIdIn(ID_COLLECTION);
    }

    @Test
    public void testFindByName() {
        final String name = "Role name";
        roleRepository.findByName(name);
        verify(roleRepository).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        roleRepository.findByOrganizationId(ORGANIZATION_ID, pageable);
        verify(roleRepository).findByOrganizationId(ORGANIZATION_ID, pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        final String name = "Role name";
        roleRepository.findByNameAndOrganizationId(name, ORGANIZATION_ID);
        verify(roleRepository).findByNameAndOrganizationId(name, ORGANIZATION_ID);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("Role name1", "Role name2");
        roleRepository.findByOrganizationIdAndNameIn(ORGANIZATION_ID, names);
        verify(roleRepository).findByOrganizationIdAndNameIn(ORGANIZATION_ID, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        roleRepository.findByOrganizationIdAndRoleIds(ORGANIZATION_ID, ID_COLLECTION, pageable);
        verify(roleRepository).findByOrganizationIdAndRoleIds(ORGANIZATION_ID, ID_COLLECTION, pageable);
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(USER_ID);
        verify(roleRepository).findByUserId(USER_ID);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(USER_ID, IS_ENABLED);
        verify(roleRepository).findByUserIdAndEnabled(USER_ID, IS_ENABLED);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        roleRepository.findByRoleIdsAndEnabled(ID_COLLECTION, IS_ENABLED);
        verify(roleRepository).findByRoleIdsAndEnabled(ID_COLLECTION, IS_ENABLED);
    }

    @Test
    public void testUpdate() {
        when(roleRepository.update(roleEntity)).thenReturn(1);
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
        verify(roleRepository).update(roleEntity);
    }
}
