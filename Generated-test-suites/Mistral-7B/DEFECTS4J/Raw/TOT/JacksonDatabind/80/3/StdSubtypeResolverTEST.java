package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import org.testng.annotations.*;

import java.util.*;

public class StdSubtypeResolverTest {

    private ObjectMapper mapper;
    private StdSubtypeResolver subtypeResolver;

    @BeforeMethod
    public void setUp() {
        mapper = new ObjectMapper();
        subtypeResolver = new StdSubtypeResolver();
        mapper.setSubtypeResolver(subtypeResolver);
    }

    @Test
    public void testRegisterSubtypesByClass() throws JsonProcessingException {
        // Test registering subtypes by class
        mapper.registerSubtypes(SubtypeA.class);
        ObjectWriter writer = mapper.writerFor(new TypeReference<List<Object>>() {});
        List<Object> list1 = writer.writeValueAsList(Arrays.asList(new SubtypeB(), new SubtypeA()));

        MapperConfig<?> config = mapper.getConfig();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, SubtypeA.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, ac);
        Assert.assertEquals(subtypes.size(), 2);
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeA.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeB.class)));
    }

    @Test
    public void testRegisterSubtypesByName() throws JsonProcessingException {
        // Test registering subtypes by name
        mapper.registerSubtypes(new NamedType(SubtypeC.class, "SubtypeC"));
        ObjectWriter writer = mapper.writerFor(new TypeReference<List<Object>>() {});
        List<Object> list2 = writer.writeValueAsList(Arrays.asList(new SubtypeA(), new SubtypeC()));

        MapperConfig<?> config = mapper.getConfig();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, SubtypeA.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, ac);
        Assert.assertEquals(subtypes.size(), 3);
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeA.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeB.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeC.class, "SubtypeC")));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() throws JsonProcessingException {
        // Test collecting and resolving subtypes by class
        mapper.registerSubtypes(SubtypeA.class);
        mapper.registerSubtypes(SubtypeB.class);
        ObjectWriter writer = mapper.writerFor(new TypeReference<List<Object>>() {});
        List<Object> list3 = writer.writeValueAsList(Arrays.asList(new SubtypeB(), new SubtypeA()));

        ObjectReader reader = mapper.readerFor(List.class);
        List<Object> list4 = reader.readValue(new ByteArrayInputStream(list3.toString().getBytes()));

        MapperConfig<?> config = mapper.getConfig();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, SubtypeA.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, ac);
        Assert.assertEquals(subtypes.size(), 2);
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeA.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeB.class)));

        ObjectReader reader2 = mapper.readerFor(SubtypeB.class);
        SubtypeB subtypeB = reader2.readValue(new ByteArrayInputStream(list3.get(1).toString().getBytes()));
        Assert.assertNotNull(subtypeB);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() throws JsonProcessingException {
        // Test collecting and resolving subtypes by type id
        mapper.registerSubtypes(SubtypeD.class);
        ObjectWriter writer = mapper.writerFor(new TypeReference<List<Object>>() {});
        List<Object> list5 = writer.writeValueAsList(Arrays.asList(new SubtypeA(), new SubtypeD()));

        ObjectReader reader = mapper.readerFor(List.class);
        List<Object> list6 = reader.readValue(new ByteArrayInputStream(list5.toString().getBytes()));

        MapperConfig<?> config = mapper.getConfig();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, SubtypeA.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, ac, null);
        Assert.assertEquals(subtypes.size(), 2);
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeA.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeD.class)));

        ObjectReader reader2 = mapper.readerFor(SubtypeD.class);
        SubtypeD subtypeD = reader2.readValue(new ByteArrayInputStream(list5.get(1).toString().getBytes()));
        Assert.assertNotNull(subtypeD);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotations() throws JsonProcessingException {
        // Test collecting and resolving subtypes by type id with annotations
        mapper.registerSubtypes(SubtypeE.class);
        ObjectWriter writer = mapper.writerFor(new TypeReference<List<Object>>() {});
        List<Object> list7 = writer.writeValueAsList(Arrays.asList(new SubtypeA(), new SubtypeE()));

        ObjectReader reader = mapper.readerFor(List.class);
        List<Object> list8 = reader.readValue(new ByteArrayInputStream(list7.toString().getBytes()));

        MapperConfig<?> config = mapper.getConfig();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, SubtypeA.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, ac, null);
        Assert.assertEquals(subtypes.size(), 3);
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeA.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeE.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeE.class, "SubtypeE")));

        ObjectReader reader2 = mapper.readerFor(SubtypeE.class);
        SubtypeE subtypeE = reader2.readValue(new ByteArrayInputStream(list7.get(1).toString().getBytes()));
        Assert.assertNotNull(subtypeE);
    }

    @Test(expectedExceptions = {JsonMappingException.class})
    public void testCollectAndResolveSubtypesByTypeIdWithInvalidType() throws JsonProcessingException {
        // Test collecting and resolving subtypes by type id with invalid type
        mapper.registerSubtypes(SubtypeF.class);
        ObjectWriter writer = mapper.writerFor(new TypeReference<List<Object>>() {});
        List<Object> list9 = writer.writeValueAsList(Arrays.asList(new SubtypeA(), new SubtypeF()));

        ObjectReader reader = mapper.readerFor(List.class);
        List<Object> list10 = reader.readValue(new ByteArrayInputStream(list9.toString().getBytes()));

        MapperConfig<?> config = mapper.getConfig();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, SubtypeA.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, ac, null);

        ObjectReader reader2 = mapper.readerFor(SubtypeF.class);
        reader2.readValue(new ByteArrayInputStream(list9.get(1).toString().getBytes()));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAbstractClass() throws JsonProcessingException {
        // Test collecting and resolving subtypes by type id with abstract class
        mapper.registerSubtypes(SubtypeG.class);
        ObjectWriter writer = mapper.writerFor(new TypeReference<List<Object>>() {});
        List<Object> list11 = writer.writeValueAsList(Arrays.asList(new SubtypeG(), new SubtypeA()));

        ObjectReader reader = mapper.readerFor(List.class);
        List<Object> list12 = reader.readValue(new ByteArrayInputStream(list11.toString().getBytes()));

        MapperConfig<?> config = mapper.getConfig();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, SubtypeG.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, ac, null);
        Assert.assertEquals(subtypes.size(), 2);
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeG.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(SubtypeA.class)));

        ObjectReader reader2 = mapper.readerFor(SubtypeA.class);
        SubtypeA subtypeA = reader2.readValue(new ByteArrayInputStream(list11.get(1).toString().getBytes()));
        Assert.assertNotNull(subtypeA);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithEmptyList() throws JsonProcessingException {
        // Test collecting and resolving subtypes by type id with empty list
        ObjectWriter writer = mapper.writerFor(new TypeReference<List<Object>>() {});
        List<Object> list13 = writer.writeValueAsList(new ArrayList<Object>());

        ObjectReader reader = mapper.readerFor(List.class);
        List<Object> list14 = reader.readValue(new ByteArrayInputStream(list13.toString().getBytes()));

        MapperConfig<?> config = mapper.getConfig();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, ac, null);
        Assert.assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithUnknownType() throws JsonProcessingException {
        // Test collecting and resolving subtypes by type id with unknown type
        mapper.registerSubtypes(SubtypeH.class);
        ObjectWriter writer = mapper.writerFor(new TypeReference<List<Object>>() {});
        List<Object> list15 = writer.writeValueAsList(Arrays.asList(new SubtypeA(), new SubtypeH()));

        ObjectReader reader = mapper.readerFor(List.class);
        List<Object> list16 = reader.readValue(new ByteArrayInputStream(list15.toString().getBytes()));

        MapperConfig<?> config = mapper.getConfig();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Integer.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, ac, null);
        Assert.assertEquals(subtypes.size(), 0);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        mapper = null;
        subtypeResolver = null;
    }
}