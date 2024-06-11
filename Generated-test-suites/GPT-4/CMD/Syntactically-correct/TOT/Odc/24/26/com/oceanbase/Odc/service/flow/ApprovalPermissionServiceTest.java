package com.oceanbase.odc.service.flow;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.metadb.iam.UserRepository;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCandidatesTest() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        UserEntity userEntity1 = new UserEntity();
        UserEntity userEntity2 = new UserEntity();
        UserTaskInstanceCandidateEntity candidate1 = new UserTaskInstanceCandidateEntity();
        UserTaskInstanceCandidateEntity candidate2 = new UserTaskInstanceCandidateEntity();
        candidate1.setUserId(1L);
        candidate2.setUserId(2L);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Arrays.asList(candidate1, candidate2));
        when(userRepository.findByUserIdsAndEnabled(new HashSet<>(Arrays.asList(candidate1.getUserId(), candidate2.getUserId())), enabled)).thenReturn(Arrays.asList(userEntity1, userEntity2));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(2, candidates.size());
    }
}