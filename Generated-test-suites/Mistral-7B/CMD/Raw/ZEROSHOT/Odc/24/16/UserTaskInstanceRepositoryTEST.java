package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.service.dto.UserTaskInstanceDTO;
import com.oceanbase.odc.metadb.flow.test.builder.UserTaskInstanceEntityBuilder;
import com.oceanbase.odc.service.flow.model.FlowNodeApprovalInstanceIdentifier;
import com.oceanbase.odc.service.flow.model.UserTaskIdentifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntityBuilder userTaskInstanceEntityBuilder;

    @Before
    public void setup() {
        userTaskInstanceEntityBuilder = new UserTaskInstanceEntityBuilder();
    }

    @Test
    public void testFindByStatus() {
        // Given
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expected = Arrays.asList(
                userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build());
        userTaskInstanceRepository.saveAll(Arrays.asList(userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build()));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(status);

        // Then
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> expected = Arrays.asList(
                userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build());
        userTaskInstanceRepository.saveAll(Arrays.asList(userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build()));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // Then
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // Given
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role_1", "role_2");
        List<UserTaskInstanceEntity> expected = Arrays.asList(
                userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build());
        userTaskInstanceRepository.saveAll(Arrays.asList(userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build()));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

        // Then
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        // Given
        Collection<String> resourceRoleIdentifier = Arrays.asList("role_1", "role_2");
        List<UserTaskInstanceEntity> expected = Arrays.asList(
                userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build());
        userTaskInstanceRepository.saveAll(Arrays.asList(userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build()));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

        // Then
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByRoleId() {
        // Given
        Long roleId = 1L;
        List<UserTaskInstanceEntity> expected = Arrays.asList(
                userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build());
        userTaskInstanceRepository.saveAll(Arrays.asList(userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build()));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(roleId);

        // Then
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserId() {
        // Given
        Long userId = 1L;
        List<UserTaskInstanceEntity> expected = Arrays.asList(
                userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build());
        userTaskInstanceRepository.saveAll(Arrays.asList(userTaskInstanceEntityBuilder.build(), userTaskInstanceEntityBuilder.build()));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(userId);

        // Then
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdate() {
        // Given
        Long id = 1L;
        UserTaskInstanceEntity entity = userTaskInstanceEntityBuilder.build();
        UserTaskInstanceEntity updatedEntity = userTaskInstanceEntityBuilder.build();

        // When
        int rowsAffected = userTaskInstanceRepository.update(updatedEntity);

        // Then
        assertNotNull(rowsAffected);
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(id).orElse(null);
        assertEquals(updatedEntity, actual);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        Long id = 1L;
        String userTaskId = "userTaskId_1";

        // When
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        // Then
        assertNotNull(rowsAffected);
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(id).orElse(null);
        assertEquals(userTaskId, actual.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        // Given
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        // When
        int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);

        // Then
        assertNotNull(rowsAffected);
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(id).orElse(null);
        assertEquals(status, actual.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;
        UserTaskInstanceEntity entity = userTaskInstanceEntityBuilder.build();
        userTaskInstanceRepository.save(entity);

        // When
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        // Then
        assertNotNull(rowsAffected);
        assertEquals(1, rowsAffected);

        // Then
        assertNull(userTaskInstanceRepository.findById(entity.getId()).orElse(null));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        Long activityId = 1L;
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity entity = userTaskInstanceEntityBuilder.build();
        userTaskInstanceRepository.save(entity);

        // When
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        // Then
        assertTrue(actual.isPresent());
        assertEquals(entity, actual.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity entity = userTaskInstanceEntityBuilder.build();
        userTaskInstanceRepository.save(entity);

        // When
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        // Then
        assertTrue(actual.isPresent());
        assertEquals(entity, actual.get());
    }

    @Test
    public void testFindConfirmById() {
        // Given
        Long id = 1L;
        boolean confirm = true;
        UserTaskInstanceEntity entity = userTaskInstanceEntityBuilder.build();
        userTaskInstanceRepository.save(entity);

        // When
        boolean actual = userTaskInstanceRepository.findConfirmById(id);

        // Then
        assertEquals(confirm, actual);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        UserTaskInstanceEntity entity1 = userTaskInstanceEntityBuilder.build();
        UserTaskInstanceEntity entity2 = userTaskInstanceEntityBuilder.build();
        userTaskInstanceRepository.saveAll(Arrays.asList(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, status);

        // Then
        assertEquals(1, actual.size());
        assertEquals(entity1, actual.get(0));
    }
}