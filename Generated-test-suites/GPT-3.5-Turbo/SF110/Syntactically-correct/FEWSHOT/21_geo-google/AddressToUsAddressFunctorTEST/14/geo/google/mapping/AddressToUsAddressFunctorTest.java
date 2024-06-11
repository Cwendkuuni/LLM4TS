import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import geo.google.mapping.*;
import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        addr.setAddressDetails(addressDetails);

        // Test 1
        GeoUsAddress expected1 = new GeoUsAddress();
        AddressDetails.Country country1 = new AddressDetails.Country();
        CountryNameCode countryNameCode1 = new CountryNameCode();
        countryNameCode1.setContent("USA");
        List<AddressDetails.Country.CountryNameCode> countryNames1 = new ArrayList<>();
        countryNames1.add(countryNameCode1);
        country1.setCountryNameCode(countryNames1);
        AdministrativeArea adminArea1 = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName1 = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName1.setContent("California");
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames1 = new ArrayList<>();
        adminAreaNames1.add(adminAreaName1);
        adminArea1.setAdministrativeAreaName(adminAreaNames1);
        addressDetails.setCountry(country1);
        country1.setAdministrativeArea(adminArea1);
        GeoUsAddress result1 = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected1.getCountry(), result1.getCountry());
        assertEquals(expected1.getState(), result1.getState());

        // Test 2
        GeoUsAddress expected2 = new GeoUsAddress();
        AdministrativeArea adminArea2 = new AdministrativeArea();
        AdministrativeArea.SubAdministrativeArea subAdminArea2 = new AdministrativeArea.SubAdministrativeArea();
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames2 = new ArrayList<>();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subName2 = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subName2.setContent("Los Angeles County");
        subNames2.add(subName2);
        subAdminArea2.setSubAdministrativeAreaName(subNames2);
        adminArea2.setSubAdministrativeArea(subAdminArea2);
        addressDetails.getCountry().setAdministrativeArea(adminArea2);
        GeoUsAddress result2 = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected2.getCounty(), result2.getCounty());

        // Test 3
        GeoUsAddress expected3 = new GeoUsAddress();
        Locality locality3 = new Locality();
        List<Locality.LocalityName> localNames3 = new ArrayList<>();
        Locality.LocalityName localName3 = new Locality.LocalityName();
        localName3.setContent("Los Angeles");
        localNames3.add(localName3);
        locality3.setLocalityName(localNames3);
        PostalCode postalCode3 = new PostalCode();
        List<PostalCode.PostalCodeNumber> numbers3 = new ArrayList<>();
        PostalCode.PostalCodeNumber number3 = new PostalCode.PostalCodeNumber();
        number3.setContent("90001");
        numbers3.add(number3);
        postalCode3.setPostalCodeNumber(numbers3);
        addressDetails.getCountry().getAdministrativeArea().setLocality(locality3);
        locality3.setPostalCode(postalCode3);
        GeoUsAddress result3 = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected3.getCity(), result3.getCity());
        assertEquals(expected3.getPostalCode(), result3.getPostalCode());

        // Test 4
        GeoUsAddress expected4 = new GeoUsAddress();
        Thoroughfare thoroughfare4 = new Thoroughfare();
        List<ThoroughfareNameType> thoroughfares4 = new ArrayList<>();
        ThoroughfareNameType thoroughfare4Name1 = new ThoroughfareNameType();
        thoroughfare4Name1.setContent("123 Main St");
        ThoroughfareNameType thoroughfare4Name2 = new ThoroughfareNameType();
        thoroughfare4Name2.setContent("Apt 4B");
        thoroughfares4.add(thoroughfare4Name1);
        thoroughfares4.add(thoroughfare4Name2);
        thoroughfare4.setThoroughfareName(thoroughfares4);
        locality3.setThoroughfare(thoroughfare4);
        GeoUsAddress result4 = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected4.getAddressLine1(), result4.getAddressLine1());
        assertEquals(expected4.getAddressLine2(), result4.getAddressLine2());

        // Test 5
        GeoUsAddress expected5 = new GeoUsAddress();
        GeoCoordinate coordinate5 = new GeoCoordinate();
        coordinate5.setLatitude(37.7749);
        coordinate5.setLongitude(-122.4194);
        addr.setCoordinate(coordinate5);
        GeoUsAddress result5 = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected5.getCoordinate(), result5.getCoordinate());
        
        // Test 6
        GeoUsAddress expected6 = new GeoUsAddress();
        expected6.setAccuracy(1.0);
        addr.setAccuracy(1.0);
        GeoUsAddress result6 = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected6.getAccuracy(), result6.getAccuracy(), 0.0);
    }
}