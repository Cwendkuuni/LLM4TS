package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@SpringJUnitConfig(classes = RoleRepositoryTestConfig.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Long firstId = 1L;
        Long secondId = 2L;
        List<Long> ids = Arrays.asList(firstId, secondId);

        List<RoleEntity> expectedResult = Arrays.asList(
                new RoleEntity(firstId, "role1", true, "desc1"),
                new RoleEntity(secondId, "role2", false, "desc2")
        );

        List<RoleEntity> actualResult = roleRepository.findByIdIn(ids);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFindByName() {
        String name = "role1";

        RoleEntity expectedResult = new RoleEntity(1L, name, true, "desc1");

        RoleEntity actualResult = roleRepository.findByName(name).orElse(null);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = Pageable.ofSize(10).sort(Sort.by("id").descending());

        Page<RoleEntity> expectedPage = Page.of(
                Arrays.asList(
                        new RoleEntity(1L, "role1", true, "desc1"),
                        new RoleEntity(2L, "role2", false, "desc2")
                ),
                Page.empty()
        );

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        Assertions.assertEquals(expectedPage.getContent(), actualPage.getContent());
        Assertions.assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;

        List<RoleEntity> expectedResult = Arrays.asList(
                new RoleEntity(1L, "role1", true, "desc1"),
                new RoleEntity(2L, "role2", false, "desc2")
        );

        Executable testMethod = () -> roleRepository.findByUserIdAndEnabled(userId, enabled);

        List<RoleEntity> actualResult = testMethod.execute();

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity(1L, "role1", true, "desc1");
        String newName = "newRoleName";
        Boolean newEnabled = false;
        String newDescription = "newDescription";

        int updatedRows = roleRepository.update(roleEntity);

        Assertions.assertEquals(1, updatedRows);

        RoleEntity updatedRoleEntity = roleRepository.findById(roleEntity.getId()).orElse(null);

        Assertions.assertEquals(newName, updatedRoleEntity.getName());
        Assertions.assertEquals(newEnabled, updatedRoleEntity.isEnabled());
        Assertions.assertEquals(newDescription, updatedRoleEntity.getDescription());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;

        List<RoleEntity> expectedResult = Arrays.asList(
                new RoleEntity(1L, "role1", true, "desc1"),
                new RoleEntity(2L, "role2", false, "desc2")
        );

        List<RoleEntity> actualResult = roleRepository.findByUserId(userId);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFindByUserIdAndEnabled_EmptyResult() {
        Long userId = 1L;
        Boolean enabled = true;

        List<RoleEntity> expectedResult = Collections.emptyList();

        List<RoleEntity> actualResult = roleRepository.findByUserIdAndEnabled(userId, enabled);

        Assertions.assertEquals(expectedResult, actualResult);
    }
}