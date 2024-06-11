import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

 @Autowired
 @InjectMocks
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 ReflectionTestUtils.setFieldValue(userTaskInstanceRepository, "userTaskInstanceEntities", userTaskInstanceEntities);

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assert.assertEquals(1, result.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // prepare mocks
 Long userId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);

 // when
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

 // then
 Assert.assertEquals(2, result.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 // prepare mocks
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

 // when
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

 // then
 Assert.assertEquals(2, result.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 // prepare mocks
 Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

 // when
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

 // then
 Assert.assertEquals(2, result.size());
 }

 @Test
 public void testFindByRoleId() {
 // prepare mocks
 Long roleId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 ReflectionTestUtils.setFieldValue(userTaskInstanceRepository, "userTaskInstanceEntities", userTaskInstanceEntities);

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
 Assert.assertEquals(1, result.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 // prepare mocks
 Long userId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 ReflectionTestUtils.setFieldValue(userTaskInstanceRepository, "userTaskInstanceEntities", userTaskInstanceEntities);

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
 Assert.assertEquals(1, result.size());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 // when
 int result = userTaskInstanceRepository.update(entity);

 // then
 Assert.assertEquals(1, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // prepare mocks
 Long id = 1L;
 String userTaskId = "userTaskId";

 // when
 int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

 // then
 Assert.assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 // prepare mocks
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 // when
 int result = userTaskInstanceRepository.updateStatusById(id, status);

 // then
 Assert.assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // prepare mocks
 Long instanceId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 ReflectionTestUtils.setFieldValue(userTaskInstanceRepository, "userTaskInstanceEntities", userTaskInstanceEntities);

 int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 Assert.assertEquals(2, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // prepare mocks
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.DECISION;
 String activityId = "activityId";
 List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 ReflectionTestUtils.setFieldValue(userTaskInstanceRepository, "userTaskInstanceEntities", userTaskInstanceEntities);
 UserTaskInstanceEntity expectedResult = new UserTaskInstanceEntity();
 ReflectionTestUtils.setFieldValue(expectedResult, "id", 1L);

 Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assert.assertEquals(expectedResult, result.get());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // prepare mocks
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.DECISION;
 String name = "name";
 List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 ReflectionTestUtils.setFieldValue(userTaskInstanceRepository, "userTaskInstanceEntities", userTaskInstanceEntities);
 UserTaskInstanceEntity expectedResult = new UserTaskInstanceEntity();
 ReflectionTestUtils.setFieldValue(expectedResult, "id", 1L);

 Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assert.assertEquals(expectedResult, result.get());
 }

 @Test
 public void testFindConfirmById() {
 // prepare mocks
 Long id = 1L;
 Boolean expectedResult = true;
 ReflectionTestUtils.setFieldValue(userTaskInstanceRepository, "userTaskInstanceEntities", Arrays.asList(new UserTaskInstanceEntity()));

 Boolean result = userTaskInstanceRepository.findConfirmById(id);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // prepare mocks
 Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
 List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 ReflectionTestUtils.setFieldValue(userTaskInstanceRepository, "userTaskInstanceEntities", userTaskInstanceEntities);

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.APPROVED);
 Assert.assertEquals(1, result.size());
 }

}