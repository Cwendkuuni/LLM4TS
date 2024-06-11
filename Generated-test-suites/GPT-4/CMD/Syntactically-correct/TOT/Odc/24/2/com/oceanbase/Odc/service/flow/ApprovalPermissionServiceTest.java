package com.oceanbase.odc.service.flow;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    ApprovalPermissionService approvalPermissionService;

    @Mock
    UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    UserRepository userRepository;

    @Test
    public void getCandidatesTest() {
        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setApprovalInstanceId(1L);
        candidateEntity.setUserId(1L);
        List<UserTaskInstanceCandidateEntity> candidateEntities = Arrays.asList(candidateEntity);

        when(userTaskCandidateRepository.findByApprovalInstanceId(1L)).thenReturn(candidateEntities);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        List<UserEntity> userEntities = Arrays.asList(userEntity);

        when(userRepository.findByUserIdsAndEnabled(Collections.singleton(1L), true)).thenReturn(userEntities);

        Set<User> result = approvalPermissionService.getCandidates(1L, true);
        assertEquals(1, result.size());
        assertEquals(1L, result.iterator().next().getId().longValue());
    }
}