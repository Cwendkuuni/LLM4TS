
package com.oceanbase.odc.metadb.iam;

import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    private final RoleEntity dummyRole = new RoleEntity();

    private final Long organizationId = 1L;
    private final Long roleId = 2L;
    private final Long userId = 3L;
    private final List<Long> roleIds = Arrays.asList(roleId);
    private final String name = "Test Role";
    private final Pageable pageable = PageRequest.of(0, 10);

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(roleIds)).thenReturn(List.of(dummyRole));
        assertEquals(dummyRole, roleRepository.findByIdIn(roleIds).get(0));
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(any())).thenReturn(List.of(dummyRole));
        assertEquals(dummyRole, roleRepository.findByName(any()).get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        final Page<RoleEntity> rolePage = new PageImpl<>(Collections.singletonList(dummyRole));
        when(roleRepository.findByOrganizationId(anyLong(), any())).thenReturn(rolePage);
        assertEquals(dummyRole, roleRepository.findByOrganizationId(anyLong(), any()).getContent().get(0));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(Optional.of(dummyRole));
        assertEquals(dummyRole, roleRepository.findByNameAndOrganizationId(anyString(), anyLong()).get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        when(roleRepository.findByOrganizationIdAndNameIn(anyLong(), anyList())).thenReturn(List.of(dummyRole));
        assertEquals(dummyRole, roleRepository.findByOrganizationIdAndNameIn(anyLong(), anyList()).get(0));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        final Page<RoleEntity> rolePage = new PageImpl<>(Collections.singletonList(dummyRole));
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(rolePage);
        assertEquals(dummyRole, roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable).getContent().get(0));
    }

    @Test
    public void testFindByUserId() {
        when(roleRepository.findByUserId(userId)).thenReturn(List.of(dummyRole));
        assertEquals(dummyRole, roleRepository.findByUserId(userId).get(0));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        when(roleRepository.findByUserIdAndEnabled(userId, true)).thenReturn(List.of(dummyRole));
        assertEquals(dummyRole, roleRepository.findByUserIdAndEnabled(userId, true).get(0));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(List.of(dummyRole));
        assertEquals(dummyRole, roleRepository.findByRoleIdsAndEnabled(roleIds, true).get(0));
    }

    @Test
    public void testUpdate() {
        when(roleRepository.update(dummyRole)).thenReturn(1);
        assertEquals(1, roleRepository.update(dummyRole));
    }
}
