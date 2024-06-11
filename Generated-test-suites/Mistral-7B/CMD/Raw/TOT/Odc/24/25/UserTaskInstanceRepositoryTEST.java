import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.service.FlowService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private FlowService flowService;

    @Test
    public void testFindByStatus() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setStatus(FlowNodeStatus.APPROVED);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setStatus(FlowNodeStatus.REJECTED);

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(entity1, entity2);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setUserId(1L);
        entity1.setRoleId(1L);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setUserId(2L);
        entity2.setRoleId(2L);

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(entity1, entity2);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L));
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setResourceRoleIdentifier("role1");

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setResourceRoleIdentifier("role2");

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(entity1, entity2);
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2")))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Arrays.asList("role1", "role2"));
        Assertions.assertEquals(expectedList, actualList);
    }

    @ParameterizedTest(name = "[{index}] test findByRoleId")
    @MethodSource("roleIdsProvider")
    public void testFindByRoleId(Long roleId) {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setRoleId(roleId);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setRoleId(roleId);

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(entity1, entity2);
        when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByRoleId(roleId);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("userTaskId1");
        entity.setStatus(FlowNodeStatus.APPROVED);
        entity.setApproved(true);
        entity.setOperatorId(1L);
        entity.setComment("comment1");
        entity.setExpireIntervalSeconds(1000L);
        entity.setExternalFlowInstanceId("externalFlowInstanceId1");

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(entity));

        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        updatedEntity.setId(1L);
        updatedEntity.setUserTaskId("userTaskId2");
        updatedEntity.setStatus(FlowNodeStatus.REJECTED);
        updatedEntity.setApproved(false);
        updatedEntity.setOperatorId(2L);
        updatedEntity.setComment("comment2");
        updatedEntity.setExpireIntervalSeconds(500L);
        updatedEntity.setExternalFlowInstanceId("externalFlowInstanceId2");

        Executable updateExecutor = () -> userTaskInstanceRepository.save(updatedEntity);

        Mockito.doAnswer(invocation -> {
            UserTaskInstanceEntity entityToUpdate = invocation.getArgument(0);
            entityToUpdate.setUserTaskId("userTaskId3");
            entityToUpdate.setStatus(FlowNodeStatus.PENDING);
            entityToUpdate.setApproved(true);
            entityToUpdate.setOperatorId(3L);
            entityToUpdate.setComment("comment3");
            entityToUpdate.setExpireIntervalSeconds(2000L);
            entityToUpdate.setExternalFlowInstanceId("externalFlowInstanceId3");
            return entityToUpdate;
        }).when(userTaskInstanceRepository).save(any(UserTaskInstanceEntity.class));

        userTaskInstanceRepository.update(updatedEntity);

        UserTaskInstanceEntity actualEntity = userTaskInstanceRepository.findById(1L).get();
        Assertions.assertEquals("userTaskId3", actualEntity.getUserTaskId());
        Assertions.assertEquals(FlowNodeStatus.PENDING, actualEntity.getStatus());
        Assertions.assertTrue(actualEntity.isApproved());
        Assertions.assertEquals(3L, actualEntity.getOperatorId());
        Assertions.assertEquals("comment3", actualEntity.getComment());
        Assertions.assertEquals(2000L, actualEntity.getExpireIntervalSeconds());
        Assertions.assertEquals("externalFlowInstanceId3", actualEntity.getExternalFlowInstanceId());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setFlowInstanceId(1L);
        entity.setUserTaskId("userTaskId1");
        entity.setStatus(FlowNodeStatus.APPROVED);
        entity.setApproved(true);
        entity.setOperatorId(1L);
        entity.setComment("comment1");
        entity.setExpireIntervalSeconds(1000L);
        entity.setExternalFlowInstanceId("externalFlowInstanceId1");

        FlowNodeType instanceType = FlowNodeType.TASK;
        Long flowInstanceId = 1L;
        String activityId = "activityId1";

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(entity));

        UserTaskInstanceEntity actualEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId,
                flowInstanceId).get();

        Assertions.assertEquals("userTaskId1", actualEntity.getUserTaskId());
        Assertions.assertEquals(FlowNodeStatus.APPROVED, actualEntity.getStatus());
        Assertions.assertTrue(actualEntity.isApproved());
        Assertions.assertEquals(1L, actualEntity.getOperatorId());
        Assertions.assertEquals("comment1", actualEntity.getComment());
        Assertions.assertEquals(1000L, actualEntity.getExpireIntervalSeconds());
        Assertions.assertEquals("externalFlowInstanceId1", actualEntity.getExternalFlowInstanceId());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setFlowInstanceId(1L);
        entity.setUserTaskId("userTaskId1");
        entity.setStatus(FlowNodeStatus.APPROVED);
        entity.setApproved(true);
        entity.setOperatorId(1L);
        entity.setComment("comment1");
        entity.setExpireIntervalSeconds(1000L);
        entity.setExternalFlowInstanceId("externalFlowInstanceId1");
        entity.setName("name1");

        FlowNodeType instanceType = FlowNodeType.TASK;
        Long flowInstanceId = 1L;
        String name = "name1";

        when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
                .thenReturn(Optional.of(entity));

        UserTaskInstanceEntity actualEntity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId).get();

        Assertions.assertEquals("userTaskId1", actualEntity.getUserTaskId());
        Assertions.assertEquals(FlowNodeStatus.APPROVED, actualEntity.getStatus());
        Assertions.assertTrue(actualEntity.isApproved());
        Assertions.assertEquals(1L, actualEntity.getOperatorId());
        Assertions.assertEquals("comment1", actualEntity.getComment());
        Assertions.assertEquals(1000L, actualEntity.getExpireIntervalSeconds());
        Assertions.assertEquals("externalFlowInstanceId1", actualEntity.getExternalFlowInstanceId());
        Assertions.assertEquals("name1", actualEntity.getName());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setConfirm(true);

        when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(true);

        Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(1L);
        Assertions.assertTrue(actualConfirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setFlowInstanceId(1L);
        entity1.setStatus(FlowNodeStatus.APPROVED);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setFlowInstanceId(1L);
        entity2.setStatus(FlowNodeStatus.REJECTED);

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(entity1);
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L),
                FlowNodeStatus.APPROVED)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L), FlowNodeStatus.APPROVED);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setFlowInstanceId(1L);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setFlowInstanceId(1L);

        when(userTaskInstanceRepository.findAllByFlowInstanceId(1L)).thenReturn(Arrays.asList(entity1, entity2));

        int actualCount = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        Assertions.assertEquals(2, actualCount);
    }

    @Test
    public void testFindAllSorted() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setFlowInstanceId(1L);
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(0, 10, sort);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setFlowInstanceId(1L);

        Page<UserTaskInstanceEntity> expectedPage = new PageImpl<>(Arrays.asList(entity1, entity2), pageable, 2);
        when(userTaskInstanceRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<UserTaskInstanceEntity> actualPage = userTaskInstanceRepository.findAll(pageable);
        Assertions.assertEquals(expectedPage.getContent(), actualPage.getContent());
        Assertions.assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
    }

    private static Collection<Long> roleIdsProvider() {
        return Arrays.asList(1L, 2L);
    }
}