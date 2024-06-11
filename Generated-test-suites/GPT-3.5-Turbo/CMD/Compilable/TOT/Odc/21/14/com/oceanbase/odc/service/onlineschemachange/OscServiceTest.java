package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        // Typical use case
        // Test when database is present
        // Verify that correct OscLockDatabaseUserInfo is returned

        // Edge case
        // Test when database is not present
        // Verify that empty OscLockDatabaseUserInfo is returned

        // Error scenario
        // Test when id is null
        // Verify that IllegalArgumentException is thrown

    }

    @Test
    public void testSwapTable() {
        // Typical use case
        // Test when schedule task and schedule are present
        // Verify that correct OscSwapTableVO is returned

        // Edge case
        // Test when schedule task is not present
        // Verify that PreConditions.validExists error is thrown

        // Edge case
        // Test when schedule is not present
        // Verify that PreConditions.validExists error is thrown

        // Error scenario
        // Test when job type is not correct
        // Verify that PreConditions.validArgumentState error is thrown

        // Error scenario
        // Test when swap table type is not correct
        // Verify that PreConditions.validArgumentState error is thrown

        // Error scenario
        // Test when manual swap table is not enabled
        // Verify that PreConditions.validArgumentState error is thrown

    }

}