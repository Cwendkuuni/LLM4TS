import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.JpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JpaTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity2.setStatus(FlowNodeStatus.REJECTED);
        userTaskInstanceRepository.saveAll(Arrays.asList(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        // Then
        assertEquals(1, actual.size());
        assertEquals(entity1, actual.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity3 = new UserTaskInstanceEntity();
        entity1.setUserId(1L);
        entity1.setRoleIds(Arrays.asList(1L, 2L));
        entity2.setUserId(2L);
        entity2.setRoleIds(Collections.singletonList(1L));
        entity3.setUserId(3L);
        entity3.setRoleIds(Collections.emptyList());
        userTaskInstanceRepository.saveAll(Arrays.asList(entity1, entity2, entity3));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));

        // Then
        assertEquals(2, actual.size());
        assertTrue(actual.contains(entity1));
        assertTrue(actual.contains(entity2));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setUserId(1L);
        entity1.setResourceRoleIdentifier("role1");
        entity2.setUserId(2L);
        entity2.setResourceRoleIdentifier("role2");
        userTaskInstanceRepository.saveAll(Arrays.asList(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));

        // Then
        assertEquals(1, actual.size());
        assertEquals(entity1, actual.get(0));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setResourceRoleIdentifier("role1");
        entity2.setResourceRoleIdentifier("role2");
        userTaskInstanceRepository.saveAll(Arrays.asList(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));

        // Then
        assertEquals(2, actual.size());
        assertTrue(actual.contains(entity1));
        assertTrue(actual.contains(entity2));
    }

    @Test
    public void testFindByRoleId() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setRoleId(1L);
        entity2.setRoleId(1L);
        userTaskInstanceRepository.saveAll(Arrays.asList(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(1L);

        // Then
        assertEquals(2, actual.size());
        assertTrue(actual.contains(entity1));
        assertTrue(actual.contains(entity2));
    }

    @Test
    public void testFindByCandidateUserId() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setUserId(1L);
        userTaskInstanceRepository.saveAll(Arrays.asList(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(1L);

        // Then
        assertEquals(1, actual.size());
        assertEquals(entity1, actual.get(0));
    }

    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Long id = 1L;
        userTaskInstanceRepository.save(entity);

        // When
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        updatedEntity.setId(id);
        updatedEntity.setUserTaskId("newUserTaskId");
        updatedEntity.setStatus(FlowNodeStatus.APPROVED);
        updatedEntity.setApproved(true);
        updatedEntity.setOperatorId(1L);
        updatedEntity.setComment("test comment");
        updatedEntity.setExpireIntervalSeconds(120);
        updatedEntity.setExternalFlowInstanceId("testExternalInstanceId");
        userTaskInstanceRepository.save(updatedEntity);

        // Then
        UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(id).orElseThrow();
        assertEquals("newUserTaskId", actual.getUserTaskId());
        assertEquals(FlowNodeStatus.APPROVED, actual.getStatus());
        assertTrue(actual.isApproved());
        assertEquals(1L, actual.getOperatorId().longValue());
        assertEquals("test comment", actual.getComment());
        assertEquals(120, actual.getExpireIntervalSeconds().intValue());
        assertEquals("testExternalInstanceId", actual.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Long id = 1L;
        entity.setId(id);
        userTaskInstanceRepository.save(entity);

        // When
        String userTaskId = "newUserTaskId";
        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        // Then
        UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(id).orElseThrow();
        assertEquals(userTaskId, actual.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Long id = 1L;
        entity.setId(id);
        userTaskInstanceRepository.save(entity);

        // When
        FlowNodeStatus status = FlowNodeStatus.REJECTED;
        userTaskInstanceRepository.updateStatusById(id, status);

        // Then
        UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(id).orElseThrow();
        assertEquals(status.name(), actual.getStatus().name());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        Long id1 = 1L;
        Long id2 = 2L;
        entity1.setId(id1);
        entity2.setId(id2);
        userTaskInstanceRepository.saveAll(Arrays.asList(entity1, entity2));

        // When
        Long instanceId = 1L;
        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        // Then
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findAll();
        assertFalse(actual.contains(entity1));
        assertTrue(actual.contains(entity2));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Long id = 1L;
        entity.setId(id);
        userTaskInstanceRepository.save(entity);

        // When
        FlowNodeType instanceType = FlowNodeType.START;
        String activityId = "activityId";
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, 1L);

        // Then
        assertTrue(actual.isPresent());
        assertEquals(entity, actual.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Long id = 1L;
        entity.setId(id);
        userTaskInstanceRepository.save(entity);

        // When
        FlowNodeType instanceType = FlowNodeType.START;
        String name = "name";
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, 1L);

        // Then
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindConfirmById() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Long id = 1L;
        entity.setId(id);
        userTaskInstanceRepository.save(entity);

        // When
        boolean actual = userTaskInstanceRepository.findConfirmById(id);

        // Then
        assertFalse(actual);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        Long id1 = 1L;
        Long id2 = 2L;
        entity1.setId(id1);
        entity2.setId(id2);
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity2.setStatus(FlowNodeStatus.REJECTED);
        userTaskInstanceRepository.saveAll(Arrays.asList(entity1, entity2));

        // When
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(flowInstanceId), status);

        // Then
        assertEquals(1, actual.size());
        assertEquals(entity1, actual.get(0));
    }
}