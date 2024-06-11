import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationInitializationWithValidPopulationLimit() {
        int populationLimit = 10;
        ListPopulation population = new ListPopulation(populationLimit);
        Assert.assertEquals(populationLimit, population.getPopulationLimit());
        Assert.assertNotNull(population.getChromosomes());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationInitializationWithNegativePopulationLimit() {
        int populationLimit = -5;
        ListPopulation population = new ListPopulation(populationLimit);
    }

    @Test
    public void testListPopulationInitializationWithEmptyChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
        Assert.assertEquals(populationLimit, population.getPopulationLimit());
        Assert.assertEquals(chromosomes.size(), population.getPopulationSize());
        Assert.assertEquals(population.getChromosomes(), chromosomes);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationInitializationWithNullChromosomeList() {
        List<Chromosome> chromosomes = null;
        int populationLimit = 10;
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationInitializationWithChromosomeListBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 2;
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
    }

    @Test
    public void testListPopulationAddChromosomeWithValidChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        Assert.assertEquals(population.getPopulationSize(), 1);
        Assert.assertEquals(population.getChromosomes().get(0), chromosome);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWithPopulationLimitReached() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testListPopulationAddChromosomesWithValidCollection() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        Assert.assertEquals(population.getPopulationSize(), 7);
        Assert.assertEquals(population.getChromosomes().size(), population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithCollectionBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        List<Chromosome> moreChromosomes = new ArrayList<>();
        moreChromosomes.add(new Chromosome());
        moreChromosomes.add(new Chromosome());
        population.addChromosomes(moreChromosomes);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListPopulationSetChromosomesWithNullCollection() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test
    public void testListPopulationSetChromosomesWithValidCollection() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        Assert.assertEquals(population.getPopulationSize(), chromosomes.size());
        Assert.assertEquals(population.getChromosomes(), chromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithCollectionBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        List<Chromosome> moreChromosomes = new ArrayList<>();
        moreChromosomes.add(new Chromosome());
        moreChromosomes.add(new Chromosome());
        population.setChromosomes(moreChromosomes);
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(population.getPopulationLimit(), 5);
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(population.getPopulationSize(), 0);
        population.addChromosome(new Chromosome());
        Assert.assertEquals(population.getPopulationSize(), 1);
        population.addChromosome(new Chromosome());
        Assert.assertEquals(population.getPopulationSize(), 2);
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        Assert.assertEquals(population.toString(), "[Lorg.apache.commons.math3.genetics.Chromosome;@" + population.hashCode() + "]");
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10.0);
        population.addChromosome(chromosome1);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(5.0);
        population.addChromosome(chromosome2);
        Chromosome expectedChromosome = chromosome1;
        Chromosome actualChromosome = population.getFittestChromosome();
        Assert.assertEquals(expectedChromosome, actualChromosome);
    }
}