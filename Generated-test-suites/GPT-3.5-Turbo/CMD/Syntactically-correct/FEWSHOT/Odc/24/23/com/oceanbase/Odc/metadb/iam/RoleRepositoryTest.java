package com.oceanbase.odc.metadb.iam;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {
    
    @Test
    public void testFindByIdIn() {
        // write your test cases here
    }
    
    @Test
    public void testFindByName() {
        // write your test cases here
    }
    
    @Test
    public void testFindByOrganizationId() {
        // write your test cases here
    }
    
    @Test
    public void testFindByNameAndOrganizationId() {
        // write your test cases here
    }
    
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        // write your test cases here
    }
    
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        // write your test cases here
    }
    
    @Test
    public void testFindByUserId() {
        // write your test cases here
    }
    
    @Test
    public void testFindByUserIdAndEnabled() {
        // write your test cases here
    }
    
    @Test
    public void testFindByRoleIdsAndEnabled() {
        // write your test cases here
    }
    
    @Test
    public void testUpdate() {
        // write your test cases here
    }
    
}