package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @org.junit.Before
    public void setup() {
        roleRepository = Mockito.spy(RoleRepositoryImpl.class);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expected = new ArrayList<RoleEntity>() {{
            add(new RoleEntity());
            add(new RoleEntity());
        }};
        Mockito.when(roleRepository.findByIdIn(Mockito.any(Collection.class))).thenReturn(expected);

        List<RoleEntity> actual = roleRepository.findByIdIn(new ArrayList<Long>() {{
            add(id1);
            add(id2);
        }});

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByIdInvalidId() {
        Mockito.when(roleRepository.findByIdIn(Mockito.any(Collection.class))).thenReturn(null);

        assertNull(roleRepository.findByIdIn(new ArrayList<Long>() {{
            add(Long.MIN_VALUE);
        }}));
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity roleEntity = new RoleEntity();
        Mockito.when(roleRepository.findByName(Mockito.eq(name))).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> actual = roleRepository.findByName(name);

        assertEquals(1, actual.size());
        assertSame(roleEntity, actual.get(0));
    }

    @Test
    public void testFindByNameInvalidName() {
        String name = null;
        Mockito.when(roleRepository.findByName(Mockito.eq(name))).thenReturn(Collections.emptyList());

        List<RoleEntity> actual = roleRepository.findByName(name);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        List<RoleEntity> expected = new ArrayList<RoleEntity>() {{
            add(new RoleEntity());
            add(new RoleEntity());
        }};
        Page<RoleEntity> page = new PageImpl<RoleEntity>(expected);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

        Page<RoleEntity> actual = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(expected, actual.getContent());
        assertEquals(2, actual.getTotalElements());
        assertEquals(pageable.getPageNumber(), actual.getNumber());
        assertEquals(pageable.getPageSize(), actual.getSize());
        assertEquals(pageable.getSort(), actual.getSort());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        Mockito.when(roleRepository.findByNameAndOrganizationId(Mockito.eq(name), Mockito.eq(organizationId)))
                .thenReturn(Optional.ofNullable(roleEntity));

        Optional<RoleEntity> actual = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(actual.isPresent());
        assertSame(roleEntity, actual.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<String>() {{
            add("testRole1");
            add("testRole2");
        }};
        List<RoleEntity> expected = new ArrayList<RoleEntity>() {{
            add(new RoleEntity());
            add(new RoleEntity());
        }};
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expected);

        List<RoleEntity> actual = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<Long>() {{
            add(1L);
            add(2L);
        }};
        List<RoleEntity> expected = new ArrayList<RoleEntity>() {{
            add(new RoleEntity());
            add(new RoleEntity());
        }};
        Page<RoleEntity> page = new PageImpl<RoleEntity>(expected);
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> actual = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                Mockito.mock(Pageable.class));

        assertEquals(expected, actual.getContent());
        assertEquals(2, actual.getTotalElements());
        assertEquals(1, actual.getNumberOfElements());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expected = new ArrayList<RoleEntity>() {{
            add(new RoleEntity());
            add(new RoleEntity());
        }};
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expected);

        List<RoleEntity> actual = roleRepository.findByUserId(userId);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expected = new ArrayList<RoleEntity>() {{
            add(new RoleEntity());
            add(new RoleEntity());
        }};
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expected);

        List<RoleEntity> actual = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<Long>() {{
            add(1L);
            add(2L);
        }};
        Boolean enabled = true;
        List<RoleEntity> expected = new ArrayList<RoleEntity>() {{
            add(new RoleEntity());
            add(new RoleEntity());
        }};
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expected);

        List<RoleEntity> actual = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        Mockito.doAnswer(invocation -> 1).when(roleRepository).update(Mockito.eq(roleEntity));

        int actual = roleRepository.update(roleEntity);

        assertEquals(1, actual);
    }
}