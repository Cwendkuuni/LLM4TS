
package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    //ToDo: Initialise a mock field for each dependency of NodeInstanceEntityRepository

    @InjectMocks
    private NodeInstanceEntityRepository repository;

    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
        // ToDo: Write your test, assert that the returned list matches your expectations
    }

    @Test
    public void findByFlowInstanceIdTest() {
        // ToDo: Write your test, assert that the returned list matches your expectations
    }

    @Test
    public void findByIdsTest() {
        // ToDo: Write your test, assert that the returned list matches your expectations
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        // ToDo: Write your test, assert that the returned int matches your expectations
    }

    @Test
    public void deleteByInstanceIdAndInstanceTypeTest() {
        // ToDo: Write your test, assert that the returned int matches your expectations
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeNullTest() {
        repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByFlowInstanceIdNullTest() {
        repository.findByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByIdsNullTest() {
        repository.findByIds(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteByFlowInstanceIdNullTest() {
        repository.deleteByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteByInstanceIdAndInstanceTypeNullTest() {
        repository.deleteByInstanceIdAndInstanceType(null, null);
    }
}
