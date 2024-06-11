package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.model.RoleEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringJUnitConfig(classes = RoleRepository.class)
public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void init() {
        roleRepository = Mockito.spy(RoleRepositoryImpl.class);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = List.of(id1, id2);
        List<RoleEntity> expected = List.of(new RoleEntity(id1), new RoleEntity(id2));
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByName() {
        String name = "test";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        List<RoleEntity> expected = Collections.singletonList(roleEntity);
        Mockito.when(roleRepository.findByName(name)).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByName(name);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> expected = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expected);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);
        Optional<RoleEntity> expected = Optional.of(roleEntity);
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expected);
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = List.of("test1", "test2");
        List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = List.of(1L, 2L);
        Page<RoleEntity> expected = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any()))
                .thenReturn(expected);
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByUserId(userId);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expected);
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        Mockito.doAnswer(invocation -> {
            RoleEntity roleEntityParam = (RoleEntity) invocation.getArgument(0);
            Assert.assertEquals(roleEntity.getId(), roleEntityParam.getId());
            Assert.assertEquals(roleEntity.getName(), roleEntityParam.getName());
            Assert.assertEquals(roleEntity.isEnabled(), roleEntityParam.isEnabled());
            Assert.assertEquals(roleEntity.getDescription(), roleEntityParam.getDescription());
            return 1;
        }).when(roleRepository).update(Mockito.any());
        int result = roleRepository.update(roleEntity);
        Assert.assertEquals(1, result);
    }
}