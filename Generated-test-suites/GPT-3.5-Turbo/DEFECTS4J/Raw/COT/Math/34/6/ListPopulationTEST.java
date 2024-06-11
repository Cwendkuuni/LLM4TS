import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        Assert.assertEquals(10, listPopulation.getPopulationLimit());
        Assert.assertEquals(0, listPopulation.getPopulationSize());
        Assert.assertNotNull(listPopulation.getChromosomes());
        Assert.assertEquals(0, listPopulation.getChromosomes().size());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructor_InvalidPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructor_TooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosome_NullArgument() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosome_TooManyChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
        Assert.assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_TooManyChromosomes() {
        ListPopulation listPopulation = new ListPopulation(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        List<Chromosome> returnedChromosomes = listPopulation.getChromosomes();
        Assert.assertEquals(2, returnedChromosomes.size());
        Assert.assertEquals(chromosome1, returnedChromosomes.get(0));
        Assert.assertEquals(chromosome2, returnedChromosomes.get(1));
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(2);
        Chromosome chromosome2 = new Chromosome(5);
        Chromosome chromosome3 = new Chromosome(3);
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2, chromosome3);
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        Chromosome fittestChromosome = listPopulation.getFittestChromosome();
        Assert.assertEquals(chromosome2, fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        Assert.assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_InvalidPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_NewSizeSmallerThanCurrent() {
        Chromosome chromosome1 = new Chromosome(2);
        Chromosome chromosome2 = new Chromosome(5);
        Chromosome chromosome3 = new Chromosome(3);
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2, chromosome3);
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        listPopulation.setPopulationLimit(2);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(10);
        Assert.assertEquals(0, listPopulation.getPopulationSize());
        listPopulation.addChromosome(new Chromosome());
        Assert.assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome(2);
        Chromosome chromosome2 = new Chromosome(5);
        Chromosome chromosome3 = new Chromosome(3);
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2, chromosome3);
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        List<Chromosome> returnedChromosomes = new ArrayList<>();
        for (Chromosome chromosome : listPopulation) {
            returnedChromosomes.add(chromosome);
        }
        Assert.assertEquals(chromosomes, returnedChromosomes);
    }

    @Test
    public void testToString() {
        Chromosome chromosome1 = new Chromosome(2);
        Chromosome chromosome2 = new Chromosome(5);
        Chromosome chromosome3 = new Chromosome(3);
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2, chromosome3);
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        String expectedString = "[Chromosome[2], Chromosome[5], Chromosome[3]]";
        Assert.assertEquals(expectedString, listPopulation.toString());
    }
}