package com.oceanbase.odc.service.flow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        final Long approvalInstanceId = 1L;

        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setRoleId(2L);
        ArrayList<UserTaskInstanceCandidateEntity> entities = new ArrayList<UserTaskInstanceCandidateEntity>();
        entities.add(entity);
   
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        Set<UserEntity> userEntities = new HashSet<UserEntity>();
        userEntities.add(userEntity);

        when(userRepository.findByRoleIdsAndEnabled(any(), any())).thenReturn(userEntities);

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);

        assertEquals(1, result.size());
        assertTrue(result.stream().filter(user -> user.getId().equals(userEntity.getId())).findAny().isPresent());

        // Write additional Test Cases to cover different scenarios including error scenarios
    }
}