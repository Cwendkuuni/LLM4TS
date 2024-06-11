package com.oceanbase.odc.service.flow;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.iam.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class ApprovalPermissionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @InjectMocks
    private ApprovalPermissionService service;

    @BeforeEach
    public void setUp() {
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(Mockito.anyLong()))
            .thenReturn(Collections.emptyList());
    }

    @AfterEach
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(userService, userTaskCandidateRepository);
    }

    @Test
    public void testGetCandidates() {
        Assertions.assertNotNull(service.getCandidates(1L, true));
        Mockito.verify(userTaskCandidateRepository).findByApprovalInstanceId(1L);
    }

    // TODO Add more test cases to cover all the methods in the ApprovalPermissionService class, as well as edge cases and exceptions
}