package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.test.model.TestUserTaskInstanceEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        List<TestUserTaskInstanceEntity> entities = Arrays.asList(new TestUserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED),
                new TestUserTaskInstanceEntity(2L, FlowNodeStatus.PENDING), new TestUserTaskInstanceEntity(3L, FlowNodeStatus.APPROVED));
        repository.saveAll(entities);

        List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(2, result.size());
        repository.deleteAll();
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<TestUserTaskInstanceEntity> entities = Arrays.asList(new TestUserTaskInstanceEntity(1L, "user1", "role1"),
                new TestUserTaskInstanceEntity(2L, "user1", "role2"), new TestUserTaskInstanceEntity(3L, "user2", "role1"),
                new TestUserTaskInstanceEntity(4L, "user2", "role1"));
        repository.saveAll(entities);

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList("role1"));
        assertEquals(3, result.size());
        repository.deleteAll();
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<TestUserTaskInstanceEntity> entities = Arrays.asList(new TestUserTaskInstanceEntity(1L, "user1", "role1"),
                new TestUserTaskInstanceEntity(2L, "user1", "role2"), new TestUserTaskInstanceEntity(3L, "user2", "role1"),
                new TestUserTaskInstanceEntity(4L, "user2", "role1"));
        repository.saveAll(entities);

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1"));
        assertEquals(3, result.size());
        repository.deleteAll();
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<TestUserTaskInstanceEntity> entities = Arrays.asList(new TestUserTaskInstanceEntity(1L, "user1", "role1"),
                new TestUserTaskInstanceEntity(2L, "user1", "role2"), new TestUserTaskInstanceEntity(3L, "user2", "role1"),
                new TestUserTaskInstanceEntity(4L, "user2", "role1"));
        repository.saveAll(entities);

        List<UserTaskInstanceEntity> result = repository.findByResourceRoleIdentifierIn(Arrays.asList("role1"));
        assertEquals(4, result.size());
        repository.deleteAll();
    }

    @Test
    public void testFindByRoleId() {
        List<TestUserTaskInstanceEntity> entities = Arrays.asList(new TestUserTaskInstanceEntity(1L, "user1", "role1"),
                new TestUserTaskInstanceEntity(2L, "user1", "role2"), new TestUserTaskInstanceEntity(3L, "user2", "role1"),
                new TestUserTaskInstanceEntity(4L, "user2", "role1"));
        repository.saveAll(entities);

        List<UserTaskInstanceEntity> result = repository.findByRoleId(1L);
        assertEquals(3, result.size());
        repository.deleteAll();
    }

    @Test
    public void testFindByCandidateUserId() {
        List<TestUserTaskInstanceEntity> entities = Arrays.asList(new TestUserTaskInstanceEntity(1L, "user1", "role1"),
                new TestUserTaskInstanceEntity(2L, "user1", "role2"), new TestUserTaskInstanceEntity(3L, "user2", "role1"),
                new TestUserTaskInstanceEntity(4L, "user2", "role1"));
        repository.saveAll(entities);

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(1L);
        assertEquals(2, result.size());
        repository.deleteAll();
    }

    @Test
    public void testUpdate() {
        TestUserTaskInstanceEntity entity = new TestUserTaskInstanceEntity(1L, "user1", "role1");
        repository.save(entity);

        TestUserTaskInstanceEntity updatedEntity = new TestUserTaskInstanceEntity(1L, "user2", "role2", FlowNodeStatus.APPROVED);
        repository.update(updatedEntity);

        UserTaskInstanceEntity result = repository.findById(1L).orElse(null);
        assertEquals(updatedEntity.getUserTaskId(), result.getUserTaskId());
        assertEquals(updatedEntity.getStatus(), result.getStatus());
        assertEquals(updatedEntity.getApproved(), result.isApproved());
        assertEquals(updatedEntity.getOperatorId(), result.getOperatorId());
        assertEquals(updatedEntity.getComment(), result.getComment());
        assertEquals(updatedEntity.getExpireIntervalSeconds(), result.getExpireIntervalSeconds());
        assertEquals(updatedEntity.getExternalFlowInstanceId(), result.getExternalFlowInstanceId());

        repository.deleteById(1L);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        TestUserTaskInstanceEntity entity = new TestUserTaskInstanceEntity(1L, "user1", "role1");
        repository.save(entity);

        repository.updateUserTaskIdById(1L, "testUserTaskId");

        UserTaskInstanceEntity result = repository.findById(1L).orElse(null);
        assertEquals("testUserTaskId", result.getUserTaskId());

        repository.deleteById(1L);
    }

    @Test
    public void testUpdateStatusById() {
        TestUserTaskInstanceEntity entity = new TestUserTaskInstanceEntity(1L, "user1", "role1", FlowNodeStatus.PENDING);
        repository.save(entity);

        repository.updateStatusById(1L, FlowNodeStatus.APPROVED);

        UserTaskInstanceEntity result = repository.findById(1L).orElse(null);
        assertEquals(FlowNodeStatus.APPROVED, result.getStatus());

        repository.deleteById(1L);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        TestUserTaskInstanceEntity entity = new TestUserTaskInstanceEntity(1L, "user1", "role1");
        repository.save(entity);

        repository.deleteByFlowInstanceId(1L);

        assertNull(repository.findById(1L).orElse(null));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        TestUserTaskInstanceEntity entity = new TestUserTaskInstanceEntity(1L, "user1", "role1");
        repository.save(entity);

        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndActivityId(FlowNodeType.ACTIVITY, "activityId", 1L);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());

        repository.deleteById(1L);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        TestUserTaskInstanceEntity entity = new TestUserTaskInstanceEntity(1L, "user1", "role1");
        repository.save(entity);

        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndName(FlowNodeType.ACTIVITY, "name", 1L);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());

        repository.deleteById(1L);
    }

    @Test
    public void testFindConfirmById() {
        TestUserTaskInstanceEntity entity = new TestUserTaskInstanceEntity(1L, "user1", "role1");
        repository.save(entity);

        assertTrue(repository.findConfirmById(1L));

        repository.deleteById(1L);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<TestUserTaskInstanceEntity> entities = Arrays.asList(new TestUserTaskInstanceEntity(1L, "user1", "role1"),
                new TestUserTaskInstanceEntity(2L, "user1", "role2"), new TestUserTaskInstanceEntity(3L, "user2", "role1"),
                new TestUserTaskInstanceEntity(4L, "user2", "role1"), new TestUserTaskInstanceEntity(5L, "user2", "role1"));
        repository.saveAll(entities);

        List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L),
                FlowNodeStatus.APPROVED);

        List<UserTaskInstanceEntity> expected = Arrays.asList(entities.get(0), entities.get(3));
        assertEquals(expected, result);
        repository.deleteAll();
    }
}

class TestUserTaskInstanceEntity {
    private Long id;
    private String userTaskId;
    private String userName;
    private String role;
    private FlowNodeStatus status;
    private Boolean approved;
    private Long operatorId;
    private String comment;
    private Integer expireIntervalSeconds;
    private String externalFlowInstanceId;

    public TestUserTaskInstanceEntity(Long id, String userName, String role, FlowNodeStatus status) {
        this.id = id;
        this.userName = userName;
        this.role = role;
        this.status = status;
    }

    public TestUserTaskInstanceEntity(Long id, String userName, String role, FlowNodeStatus status, Boolean approved,
            Long operatorId, String comment, Integer expireIntervalSeconds, String externalFlowInstanceId) {
        this.id = id;
        this.userTaskId = userTaskId;
        this.userName = userName;
        this.role = role;
        this.status = status;
        this.approved = approved;
        this.operatorId = operatorId;
        this.comment = comment;
        this.expireIntervalSeconds = expireIntervalSeconds;
        this.externalFlowInstanceId = externalFlowInstanceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserTaskId() {
        return userTaskId;
    }

    public void setUserTaskId(String userTaskId) {
        this.userTaskId = userTaskId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public FlowNodeStatus getStatus() {
        return status;
    }

    public void setStatus(FlowNodeStatus status) {
        this.status = status;
    }

    public Boolean isApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getExpireIntervalSeconds() {
        return expireIntervalSeconds;
    }

    public void setExpireIntervalSeconds(Integer expireIntervalSeconds) {
        this.expireIntervalSeconds = expireIntervalSeconds;
    }

    public String getExternalFlowInstanceId() {
        return externalFlowInstanceId;
    }

    public void setExternalFlowInstanceId(String externalFlowInstanceId) {
        this.externalFlowInstanceId = externalFlowInstanceId;
    }
}