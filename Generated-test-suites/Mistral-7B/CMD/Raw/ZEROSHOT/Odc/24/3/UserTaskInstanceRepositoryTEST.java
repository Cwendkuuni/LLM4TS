import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Autowired
 public void setUserTaskInstanceRepository(UserTaskInstanceRepository userTaskInstanceRepository) {
 this.userTaskInstanceRepository = userTaskInstanceRepository;
 }

 @Before
 public void setup() {
 // Initialize mocks and test data here if needed
 }

 @Test
 public void testFindByStatus() {
 // Given
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(status);
 // Then
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 // More assertions or verification of the returned objects if needed
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 Long userId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 // Then
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 // More assertions or verification of the returned objects if needed
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 // Given
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 // Then
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 // More assertions or verification of the returned objects if needed
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 // Given
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 // Then
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 // More assertions or verification of the returned objects if needed
 }

 @Test
 public void testFindByRoleId() {
 // Given
 Long roleId = 1L;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
 // Then
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 // More assertions or verification of the returned objects if needed
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 Long userId = 1L;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);
 // Then
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 // More assertions or verification of the returned objects if needed
 }

 @Test
 public void testUpdate() {
 // Given
 Long id = 1L;
 String userTaskId = "task1";
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 Long operatorId = 1L;
 String comment = "comment1";
 Long expireIntervalSeconds = 10L;
 String externalFlowInstanceId = "instance1";
 UserTaskInstanceEntity entity = Mockito.spy(new UserTaskInstanceEntity());
 // When
 int rowsAffected = userTaskInstanceRepository.update(entity);
 // Then
 Assert.assertEquals(1, rowsAffected);
 // More assertions or verification of the updated entity or database if needed
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 Long id = 1L;
 String userTaskId = "task1";
 // When
 int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 // Then
 Assert.assertEquals(1, rowsAffected);
 // More assertions or verification of the updated entity or database if needed
 }

 @Test
 public void testUpdateStatusById() {
 // Given
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 // When
 int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
 // Then
 Assert.assertEquals(1, rowsAffected);
 // More assertions or verification of the updated entity or database if needed
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;
 // When
 int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 // Then
 Assert.assertEquals(1, rowsAffected);
 // More assertions or verification of the deleted entities or database if needed
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activity1";
 Long flowInstanceId = 1L;
 // When
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 // Then
 Assert.assertTrue(userTaskInstanceEntityOptional.isPresent());
 UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
 // More assertions or verification of the returned object if needed
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name1";
 Long flowInstanceId = 1L;
 // When
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 // Then
 Assert.assertTrue(userTaskInstanceEntityOptional.isPresent());
 UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
 // More assertions or verification of the returned object if needed
 }

 @Test
 public void testFindConfirmById() {
 // Given
 Long id = 1L;
 // When
 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 // Then
 Assert.assertNotNull(confirm);
 Assert.assertEquals(true, confirm);
 // More assertions or verification of the returned value if needed
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 Collection<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 // Then
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 // More assertions or verification of the returned objects if needed
 }

 @Test
 public void testFindAll() {
 // Given
 // When
 Page<UserTaskInstanceEntity> page = userTaskInstanceRepository.findAll(PageRequest.of(0, 10));
 // Then
 Assert.assertNotNull(page);
 Assert.assertEquals(10, page.getSize());
 // More assertions or verification of the returned Page object if needed
 }

 @Test
 public void testFindAllWithPageable() {
 // Given
 Pageable pageable = PageRequest.of(0, 10);
 // When
 Page<UserTaskInstanceEntity> page = userTaskInstanceRepository.findAll(pageable);
 // Then
 Assert.assertNotNull(page);
 Assert.assertEquals(10, page.getSize());
 // More assertions or verification of the returned Page object if needed
 }

}