package com.oceanbase.odc.metadb.iam.test;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        RoleEntity roleEntity1 = Mockito.spy(new RoleEntity());
        RoleEntity roleEntity2 = Mockito.spy(new RoleEntity());
        Mockito.when(roleRepository.findById(id1)).thenReturn(Optional.of(roleEntity1));
        Mockito.when(roleRepository.findById(id2)).thenReturn(Optional.of(roleEntity2));

        Collection<Long> ids = Arrays.asList(id1, id2);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);

        Assertions.assertEquals(2, roles.size());
        Assertions.assertNotNull(roles.get(0));
        Assertions.assertNotNull(roles.get(1));
    }

    @Test
    public void testFindByName() {
        String name = "test";
        RoleEntity roleEntity = Mockito.spy(new RoleEntity());
        Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> roles = roleRepository.findByName(name);

        Assertions.assertEquals(1, roles.size());
        Assertions.assertNotNull(roles.get(0));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Long roleId1 = 1L;
        Long roleId2 = 2L;
        RoleEntity roleEntity1 = Mockito.spy(new RoleEntity());
        RoleEntity roleEntity2 = Mockito.spy(new RoleEntity());
        Mockito.when(roleRepository.findById(roleId1)).thenReturn(Optional.of(roleEntity1));
        Mockito.when(roleRepository.findById(roleId2)).thenReturn(Optional.of(roleEntity2));

        Collection<Long> roleIds = Arrays.asList(roleId1, roleId2);

        Page<RoleEntity> pages = roleRepository.findByOrganizationIdAndRoleIds(organizationId, new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 10;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public List<Sort.Order> getSort() {
                return Collections.emptyList();
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isPaged() {
                return false;
            }
        });

        Assertions.assertEquals(2, pages.getContent().size());
        Assertions.assertNotNull(pages.getContent().get(0));
        Assertions.assertNotNull(pages.getContent().get(1));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        RoleEntity roleEntity = Mockito.spy(new RoleEntity());
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(Arrays.asList(roleEntity));

        List<RoleEntity> roles = roleRepository.findByUserId(userId);

        Assertions.assertEquals(1, roles.size());
        Assertions.assertNotNull(roles.get(0));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        RoleEntity roleEntity = Mockito.spy(new RoleEntity());
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Arrays.asList(roleEntity));

        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);

        Assertions.assertEquals(1, roles.size());
        Assertions.assertNotNull(roles.get(0));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        RoleEntity roleEntity1 = Mockito.spy(new RoleEntity());
        RoleEntity roleEntity2 = Mockito.spy(new RoleEntity());
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        Assertions.assertEquals(2, roles.size());
        Assertions.assertNotNull(roles.get(0));
        Assertions.assertNotNull(roles.get(1));
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        String newName = "newName";
        Boolean newEnabled = true;
        String newDescription = "newDescription";

        RoleEntity roleEntity = Mockito.spy(new RoleEntity());
        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.of(roleEntity));

        int result = roleRepository.update(roleEntity);

        Assertions.assertEquals(1, result);
        Mockito.verify(roleEntity).setName(newName);
        Mockito.verify(roleEntity).setEnabled(newEnabled);
        Mockito.verify(roleEntity).setDescription(newDescription);
    }
}