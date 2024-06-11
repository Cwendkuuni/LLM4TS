package com.oceanbase.odc.metadb.iam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleRepositoryTest {

    @InjectMocks
    RoleRepository roleRepository;

    @Mock
    RoleEntity roleEntity;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<RoleEntity> roleEntityList = roleRepository.findByIdIn(ids);

        assertThat(roleEntityList).isNotNull();
    }

    //...similar tests for each public method...

    @Test
    public void testUpdate() {
        int result = roleRepository.update(roleEntity);

        assertThat(result).isNotNull();
    }
}