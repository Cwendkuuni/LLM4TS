package com.oceanbase.odc.metadb.flow;
import org.junit.Test;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository repository;

    @Test
    public void findByStatus() {
        // Prepare data and mock services

        // Call service

        // Do asserts

        // Verify behavior
    }

    @Test
    public void findByCandidateUserIdOrRoleIds() {
        // Prepare data and mock services

        // Call service

        // Do asserts

        // Verify behavior
    }

    // Add here similar methods following the pattern for all methods in the Repository

    @Test
    public void findApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Prepare data and mock services

        // Call service

        // Do asserts

        // Verify behavior
    }
}