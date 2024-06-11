import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    RoleRepository roleRepository;

    @Mock
    RoleEntity roleEntity;
    @Mock
    List<RoleEntity> roleEntities;
    @Mock
    Page<RoleEntity> roleEntityPage;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> expectedEntities = Collections.singletonList(roleEntity);

        when(roleRepository.findByIdIn(ids)).thenReturn(expectedEntities);

        List<RoleEntity> actualEntities = roleRepository.findByIdIn(ids);

        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        List<RoleEntity> expectedEntities = Collections.singletonList(roleEntity);

        when(roleRepository.findByName(name)).thenReturn(expectedEntities);

        List<RoleEntity> actualEntities = roleRepository.findByName(name);

        assertEquals(expectedEntities, actualEntities);
    }

    // Additional test cases can be added for other methods in RoleRepository class

}