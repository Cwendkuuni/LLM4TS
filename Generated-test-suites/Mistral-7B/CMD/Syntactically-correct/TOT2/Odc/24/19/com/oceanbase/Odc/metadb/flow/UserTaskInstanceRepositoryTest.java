import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Autowired
 public void setUserTaskInstanceRepository(UserTaskInstanceRepository userTaskInstanceRepository) {
 this.userTaskInstanceRepository = userTaskInstanceRepository;
 }

 /**
 * Test for findByStatus method with valid status
 */
 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 }

 /**
 * Test for findByStatus method with invalid status
 */
 @Test(expected = IllegalArgumentException.class)
 public void testFindByStatusWithInvalidStatus() {
 userTaskInstanceRepository.findByStatus(null);
 }

 /**
 * Test for findByCandidateUserIdOrRoleIds method with valid userId and roleIds
 */
 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());

 List<UserTaskInstanceEntity> userTaskInstanceEntities1 = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Arrays.asList(1L, 2L));
 Assert.assertNotNull(userTaskInstanceEntities1);
 Assert.assertEquals(2, userTaskInstanceEntities1.size());
 }

 /**
 * Test for findByCandidateUserIdOrResourceRoleIdentifier method with valid userId and resourceRoleIdentifier
 */
 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, null);
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());

 List<UserTaskInstanceEntity> userTaskInstanceEntities1 = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, Arrays.asList("role1", "role2"));
 Assert.assertNotNull(userTaskInstanceEntities1);
 Assert.assertEquals(2, userTaskInstanceEntities1.size());
 }

 /**
 * Test for findByResourceRoleIdentifierIn method with valid resourceRoleIdentifier
 */
 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1"));
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 }

 /**
 * Test for findByRoleId method with valid roleId
 */
 @Test
 public void testFindByRoleId() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(1L);
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 }

 /**
 * Test for findByCandidateUserId method with valid userId
 */
 @Test
 public void testFindByCandidateUserId() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(1L);
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(1, userTaskInstanceEntities.size());
 }

 /**
 * Test for update method with valid parameters
 */
 @Test
 @Transactional
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 int rowsAffected = userTaskInstanceRepository.update(entity);
 Assert.assertEquals(1, rowsAffected);
 }

 /**
 * Test for updateUserTaskIdById method with valid id and userTaskId
 */
 @Test
 @Transactional
 public void testUpdateUserTaskIdById() {
 int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");
 Assert.assertEquals(1, rowsAffected);
 }

 /**
 * Test for updateStatusById method with valid id and status
 */
 @Test
 @Transactional
 public void testUpdateStatusById() {
 int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.REJECTED);
 Assert.assertEquals(1, rowsAffected);
 }

 /**
 * Test for deleteByFlowInstanceId method with valid instanceId
 */
 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
 Assert.assertEquals(1, rowsAffected);
 }

 /**
 * Test for findByInstanceTypeAndActivityId method with valid parameters
 */
 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);
 Assert.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 /**
 * Test for findByInstanceTypeAndName method with valid parameters
 */
 @Test
 public void testFindByInstanceTypeAndName() {
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);
 Assert.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 /**
 * Test for findConfirmById method with valid id
 */
 @Test
 public void testFindConfirmById() {
 Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
 Assert.assertEquals(true, confirm);
 }

 /**
 * Test for findApprovalInstanceIdByFlowInstanceIdAndStatus method with valid flowInstanceIds and status
 */
 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
 Assert.assertNotNull(userTaskInstanceEntities);
 Assert.assertEquals(2, userTaskInstanceEntities.size());
 }

 /**
 * Test for findAll method
 */
 @Test
 public void testFindAll() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findAll(pageable);
 Assert.assertNotNull(userTaskInstanceEntities.getContent());
 Assert.assertEquals(10, userTaskInstanceEntities.getSize());
 }
}